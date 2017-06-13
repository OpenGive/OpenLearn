import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AdminService} from "../../../../../services/admin.service";
import {AdminDialogComponent} from "../../../admin-dialog.component";
import {AdminModel} from "../../../admin.constants";

@Component({
  selector: 'admin-organizations-form',
  templateUrl: './admin-organizations-form.component.html',
  styleUrls: ['./admin-organizations-form.component.css', '../../admin-forms.css']
})
export class AdminOrganizationsFormComponent implements OnInit {

  @Input('item') formOrganization: any;
  @Input() adding: boolean;
  editing: boolean;

  organizationForm: FormGroup;
  formErrors = {
    name: '',
    description: ''
  };
  validationMessages = {
    name: {
      required: 'Name is required',
      minlength: 'Name must be at least 3 characters long',
      maxlength: 'Name cannot be more than 100 characters long'
    },
    description: {
      minlength: 'Description must be at least 10 characters long',
      maxlength: 'Description cannot be more than 800 characters long'
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              private adminService: AdminService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
  }

  private buildForm(): void {
    this.organizationForm = this.fb.group({
      name: [this.formOrganization.name, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(100)
      ]],
      description: [this.formOrganization.description, [
        Validators.minLength(10),
        Validators.maxLength(800)
      ]]
    });
    this.organizationForm.valueChanges.subscribe(data => this.onValueChanged(data));
    this.onValueChanged();
  }

  private onValueChanged(data?: any): void {
    if (this.organizationForm) {
      const form = this.organizationForm;
      for (const field in this.formErrors) {
        this.formErrors[field] = '';
        const control = form.get(field);
        if (control && control.dirty && !control.valid) {
          const messages = this.validationMessages[field];
          for (const key in control.errors) {
            this.formErrors[field] += messages[key] + ' ';
          }
        }
      }
    }
  }

  private setEditing(editing): void {
    if (this.organizationForm) {
      if (editing) {
        this.organizationForm.enable();
        this.editing = true;
      } else {
        this.organizationForm.disable();
        this.editing = false;
      }
    }
  }

  save(): void {
    if (this.organizationForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.organizationForm.markAsTouched();
    }
  }

  private add(): void {
    this.adminService.create(AdminModel.Organization.route, this.organizationForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminModel.Organization.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
    });
  }

  private prepareToUpdate() {
    return {
      id: this.formOrganization.id,
      name: this.organizationForm.get('name').value,
      description: this.organizationForm.get('description').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminModel.Organization.route, this.formOrganization.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formOrganization.id
        }
      });
    });
  }

  edit(): void {
    this.setEditing(true);
  }

  cancel(): void {
    this.organizationForm.setValue({
      name: this.formOrganization.name,
      description: this.formOrganization.description,
    });
    this.setEditing(false);
  }

  close(): void {
    this.dialogRef.close();
  }
}
