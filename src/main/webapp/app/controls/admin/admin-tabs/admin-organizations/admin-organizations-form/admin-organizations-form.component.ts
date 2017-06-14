import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";

import {AdminDialogComponent} from "../../../admin-dialog.component";
import {AdminModel} from "../../../admin.constants";
import {AdminService} from "../../../../../services/admin.service";
import {NotifyService} from "../../../../../services/notify.service";

@Component({
  selector: 'admin-organizations-form',
  templateUrl: './admin-organizations-form.component.html',
  styleUrls: ['../../admin-forms.css']
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
              private adminService: AdminService,
              private notify: NotifyService) {}

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
    this.organizationForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
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

  private setEditing(editing: boolean): void {
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
      this.notify.success('Successfully added organization');
    }, error => {
      this.notify.error('Failed to add organization');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminModel.Organization.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated organization');
    }, error => {
      this.notify.error('Failed to update organization');
    });
  }

  private prepareToUpdate(): any {
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
      this.notify.success('Successfully deleted organization');
    }, error => {
      this.notify.error('Failed to delete organization');
    });
  }

  edit(): void {
    this.setEditing(true);
  }

  cancel(): void {
    this.ngOnInit();
  }

  close(): void {
    this.dialogRef.close();
  }
}
