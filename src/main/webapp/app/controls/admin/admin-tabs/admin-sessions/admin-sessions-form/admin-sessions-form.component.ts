import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";

import {AdminModel} from "../../../admin.constants";
import {AdminService} from "../../../../../services/admin.service";

@Component({
  selector: 'admin-sessions-form',
  templateUrl: './admin-sessions-form.component.html',
  styleUrls: ['./admin-sessions-form.component.css', '../../admin-forms.css']
})
export class AdminSessionsFormComponent implements OnInit {

  @Input('item') formSession: any;
  @Input() adding: boolean;
  editing: boolean;

  organizations: any[];

  sessionForm: FormGroup;
  formErrors = {
    name: '',
    description: '',
    organization: '',
    startDate: '',
    endDate: ''
  };
  validationMessages = {
    name: {
      required: 'Name is required',
      minlength: 'Name must be at least 5 characters long',
      maxlength: 'Name cannot be more than 100 characters long'
    },
    description: {
      minlength: 'Description must be at least 5 characters long',
      maxlength: 'Description cannot be more than 200 characters long'
    },
    organization: {
      required: 'Organization is required'
    },
    startDate: {
      mdDatepickerMax: 'Start date must not be after End Date'
    },
    endDate: {
      mdDatepickerMin: 'End date must not be before Start Date'
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminSessionsFormComponent>,
              private fb: FormBuilder,
              private adminService: AdminService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.getOrganizations();
  }

  private buildForm(): void {
    this.sessionForm = this.fb.group({
      name: [this.formSession.name, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      description: [this.formSession.description, [
        Validators.minLength(5),
        Validators.maxLength(200)
      ]],
      organization: [this.formSession.organization, [
        Validators.required
      ]],
      startDate: [this.formSession.startDate],
      endDate: [this.formSession.endDate],
      active: [this.formSession.active || false]
    });
    this.sessionForm.valueChanges.subscribe(data => this.onValueChanged(data));
    this.onValueChanged();
  }

  onValueChanged(data?: any): void {
    if (this.sessionForm) {
      const form = this.sessionForm;
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
    if (this.sessionForm) {
      if (editing) {
        this.sessionForm.enable();
        this.editing = true;
      } else {
        this.sessionForm.disable();
        this.editing = false;
      }
    }
  }

  private getOrganizations(): void {
    this.adminService.getAll(AdminModel.Organization.route).subscribe(resp => {
      this.organizations = resp;
    });
  }

  save(): void {
    if (this.sessionForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.sessionForm.markAsTouched();
    }
  }

  private add(): void {
    this.adminService.update(AdminModel.Session.route, this.sessionForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminModel.Session.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
    });
  }

  private prepareToUpdate() {
    return {
      id: this.formSession.id,
      name: this.sessionForm.get('name').value,
      description: this.sessionForm.get('description').value,
      organization: this.sessionForm.get('organization').value,
      startDate: this.sessionForm.get('startDate').value,
      endDate: this.sessionForm.get('endDate').value,
      active: this.sessionForm.get('active').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminModel.Session.route, this.formSession.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formSession.id
        }
      });
    });
  }

  edit(): void {
    this.setEditing(true);
  }

  cancel(): void {
    this.sessionForm.setValue({
      name: this.formSession.name,
      description: this.formSession.description,
      organization: this.formSession.organization,
      startDate: this.formSession.startDate,
      endDate: this.formSession.endDate,
      active: this.formSession.active
    });
    this.setEditing(false);
  }

  close(): void {
    this.dialogRef.close();
  }

  displayOrganization(organization): string {
    return organization ? organization.name : '';
  }
}
