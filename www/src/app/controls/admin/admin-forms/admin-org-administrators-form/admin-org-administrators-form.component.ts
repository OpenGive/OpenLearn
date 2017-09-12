import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";
import * as _ from "lodash";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AdminService} from "../../../../services/admin.service";
import {AppConstants} from "../../../../app.constants";
import {NotifyService} from "../../../../services/notify.service";
import {UserService} from "../../../../services/user.service";

@Component({
  selector: 'admin-org-administrators-form',
  templateUrl: './admin-org-administrators-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminOrgAdministratorsFormComponent implements OnInit {

  @Input('item') formOrgAdministrator: any;
  @Input() adding: boolean;
  @Input('organizations') organizations: any[];
  editing: boolean;
  changingPassword: boolean;

  roles: string[];
  states: any[];

  filteredStates: Observable<any[]>;

  orgAdministratorForm: FormGroup;
  formErrors = {
    firstName: '',
    lastName: '',
    login: '',
    password: '',
    notes: '',
    email: '',
    phoneNumber: '',
    organizationId: '',
    streetAddress1: '',
    streetAddress2: '',
    city: '',
    postalCode: ''
  };
  validationMessages = {
    firstName: {
      maxlength: 'First Name cannot be more than 50 characters long'
    },
    lastName: {
      maxlength: 'Last Name cannot be more than 50 characters long'
    },
    login: {
      required: 'Username is required',
      pattern: 'Username contains invalid characters',
      maxlength: 'Username cannot be more than 100 characters long'
    },
    password: {
      required: 'Password is required',
      minlength: 'Password must be at least 6 characters long',
      maxlength: 'Password cannot be more than 50 characters long'
    },
    notes: {
      maxlength: 'Notes cannot be more than 2000 characters long'
    },
    email: {
      // email: 'Email is not formatted correctly', TODO: See comment in buildForm()
      pattern: 'Email is not formatted correctly',
      minlength: 'Email must be at least 5 characters long',
      maxlength: 'Email cannot be more than 100 characters long'
    },
    phoneNumber: {
      pattern: 'Phone is not formatted correctly',
      maxlength: 'Phone cannot be more than 15 characters long'
    },
    organizationId: {
      required: 'Organization is required'
    },
    streetAddress1: {
      minlength: 'Street Address 1 must be at least 5 characters long',
      maxlength: 'Street Address 1 cannot be more than 50 characters long'
    },
    streetAddress2: {
      minlength: 'Street Address 2 must be at least 5 characters long',
      maxlength: 'Street Address 2 cannot be more than 50 characters long'
    },
    city: {
      maxlength: 'City cannot be more than 50 characters long'
    },
    postalCode: {
      pattern: 'Postal Code is not formatted correctly'
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              private userService: UserService,
              private notify: NotifyService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.resetPassword(false);
    this.getRoles();
    this.getStates();
  }

  private buildForm(): void {
    this.orgAdministratorForm = this.fb.group({
      firstName: [this.formOrgAdministrator.firstName, [
        Validators.maxLength(50)
      ]],
      lastName: [this.formOrgAdministrator.lastName, [
        Validators.maxLength(50)
      ]],
      login: [this.formOrgAdministrator.login, [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Login),
        Validators.maxLength(50)
      ]],
      password: [this.formOrgAdministrator.password, this.adding ? [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(50)
      ] : []],
      organizationId: [this.formOrgAdministrator.organizationId, [
        Validators.required
      ]],
      notes: [this.formOrgAdministrator.notes, [
        Validators.maxLength(2000)
      ]],
      email: [this.formOrgAdministrator.email, [
        // Validators.email, TODO: This forces email to be required, https://github.com/angular/angular/pull/16902 is the fix, pattern below is the workaround
        Validators.pattern(AppConstants.OLValidators.Email),
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      phoneNumber: [this.formOrgAdministrator.phoneNumber, [
        // TODO: Pattern
        Validators.maxLength(15)
      ]],
      streetAddress1: [this.formOrgAdministrator.streetAddress1, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      streetAddress2: [this.formOrgAdministrator.streetAddress2, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      city: [this.formOrgAdministrator.city, [
        Validators.maxLength(50)
      ]],
      state: [this.formOrgAdministrator.state],
      postalCode: [this.formOrgAdministrator.postalCode, [
        Validators.pattern(AppConstants.OLValidators.PostalCode)
      ]]
    });
    this.orgAdministratorForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.orgAdministratorForm) {
      const form = this.orgAdministratorForm;
      this.updateFormErrors(form, this.formErrors, this.validationMessages);
    }
  }

  // Recursive method to account for nested FormGroups
  private updateFormErrors(form: FormGroup, formErrors: any, validationMessages: any): void {
    for (const field in formErrors) {
      const control = form.get(field);
      if (control instanceof FormGroup) {
        this.updateFormErrors(control, formErrors[field], validationMessages[field]);
      } else {
        formErrors[field] = '';
        if (control && control.dirty && !control.valid) {
          const messages = validationMessages[field];
          for (const key in control.errors) {
            formErrors[field] += messages[key] + ' ';
          }
        }
      }
    }
  }

  private setEditing(editing: boolean): void {
    if (this.orgAdministratorForm) {
      if (editing) {
        this.orgAdministratorForm.enable();
        this.editing = true;
      } else {
        this.orgAdministratorForm.disable();
        this.editing = false;
      }
    }
  }

  private getRoles(): void {
    this.roles = Object.keys(AppConstants.Role).map(key => AppConstants.Role[key]);
  }

  private getStates(): void {
    this.states = AppConstants.States;
    this.filteredStates = this.orgAdministratorForm.get('state')
      .valueChanges
      .startWith(null)
      .map(val => val ? this.filterStates(val) : this.states.slice());
  }

  private filterStates(val: string): any[] {
    return this.states.filter(state => new RegExp(`${val}`, 'gi').test(state.name));
  }

  private setOrganizationID(): void {
    if (this.orgAdministratorForm.valid && this.orgAdministratorForm.get('organizationIds').value != null) {
      this.orgAdministratorForm.get('organizationIds').setValue([this.orgAdministratorForm.get('organizationIds').value[0]])
    }
  }

  save(): void {
    if (this.orgAdministratorForm.valid) {
      this.setOrganizationID();
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.orgAdministratorForm.markAsTouched();
    }
  }

  private add(): void {
    this.userService.create(this.orgAdministratorForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added org administrator');
    }, error => {
      this.notify.error('Failed to add org administrator');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.userService.update(toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated org administrator');
    }, error => {
      this.notify.error('Failed to update org administrator');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formOrgAdministrator.id,
      firstName: this.orgAdministratorForm.get('firstName').value,
      lastName: this.orgAdministratorForm.get('lastName').value,
      login: this.orgAdministratorForm.get('login').value,
      password: this.orgAdministratorForm.get('password').value,
      authority: AppConstants.Role.OrgAdmin,
      biography: this.orgAdministratorForm.get('biography').value,
      email: this.orgAdministratorForm.get('email').value,
      phoneNumber: this.orgAdministratorForm.get('phoneNumber').value,
      streetAddress1: this.orgAdministratorForm.get('streetAddress1').value,
      streetAddress2: this.orgAdministratorForm.get('streetAddress2').value,
      city: this.orgAdministratorForm.get('city').value,
      state: this.orgAdministratorForm.get('state').value,
      postalCode: this.orgAdministratorForm.get('postalCode').value,
      organizationId: this.orgAdministratorForm.get('organizationId').value,
    };
  }

  delete(): void {
    this.userService.delete(this.formOrgAdministrator.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formOrgAdministrator.id
        }
      });
      this.notify.success('Successfully deleted org administrator');
    }, error => {
      this.notify.error('Failed to delete org administrator');
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

  resetPassword(changingPassword: boolean): void {
    this.changingPassword = changingPassword;
  }

  displayState(stateValue: string): string {
    return stateValue ? _.filter(AppConstants.States, {value: stateValue})[0].name : '';
  }
}
