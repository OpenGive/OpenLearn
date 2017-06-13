import {Component, Input, OnChanges, OnInit, SimpleChanges} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AdminService} from "../../../../../services/admin.service";
import {AdminDialogComponent} from "../../../admin-dialog.component";

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
              private adminService: AdminService) {
  }

  ngOnInit(): void {
    if (this.adding) {
      this.insertBlanks();
    }
    this.buildForm();
    this.setEditing(this.adding);
  }

  private insertBlanks(): void {
    this.formOrganization = {
      name: '',
      description: ''
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

  save(): void {
    if (this.adding) {
      this.add();
    } else {
      this.update();
    }
  }

  private add(): void {
    console.log('ADD');
  }

  private update(): void {
    console.log('UPDATE');
  }

  cancel(close?: boolean): void {
    this.organizationForm.setValue({
      name: this.formOrganization.name,
      description: this.formOrganization.description,
    });
    if (this.adding || close) {
      this.dialogRef.close();
    } else {
      this.setEditing(false);
    }
  }

  edit(): void {
    this.setEditing(true);
  }

  delete(): void {
    console.log('DELETE');
  }
}
