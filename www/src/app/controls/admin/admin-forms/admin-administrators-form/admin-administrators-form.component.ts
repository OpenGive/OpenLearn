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
import {Admin} from "../../../../models/admin.model";
import {AdminTabs} from "../../admin.constants";

@Component({
  selector: 'admin-administrators-form',
  templateUrl: './admin-administrators-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminAdministratorsFormComponent implements OnInit {

  @Input('item') formAdministrator: Admin;
  @Input() adding: boolean;
  @Input('organizations') organizations: any[];
  editing: boolean;
  changingPassword: boolean;

  roles: string[];
  states: any[];

  filteredStates: Observable<any[]>;

  administratorForm: FormGroup;
  formErrors = {
    firstName: '',
    lastName: '',
    login: '',
    password: '',
    email: '',
    phoneNumber: '',
    streetAddress1: '',
    streetAddress2: '',
    city: '',
    postalCode: '',
    notes: ''
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
    },
    notes: {
      maxlength: 'Notes cannot be more than 2000 characters long'
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              private userService: UserService,
              private notify: NotifyService,
              private adminService: AdminService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.resetPassword(false);
    this.getRoles();
    this.getStates();
  }

  private buildForm(): void {
    this.administratorForm = this.fb.group({
      firstName: [this.formAdministrator.firstName, [
        Validators.maxLength(50)
      ]],
      lastName: [this.formAdministrator.lastName, [
        Validators.maxLength(50)
      ]],
      login: [this.formAdministrator.login, [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Login),
        Validators.maxLength(50)
      ]],
      password: [this.formAdministrator.password, this.adding ? [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(50)
      ] : []],
      email: [this.formAdministrator.email, [
        // Validators.email, TODO: This forces email to be required, https://github.com/angular/angular/pull/16902 is the fix, pattern below is the workaround
        Validators.pattern(AppConstants.OLValidators.Email),
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      phoneNumber: [this.formAdministrator.phoneNumber, [
        // TODO: Pattern
        Validators.maxLength(15)
      ]],
      streetAddress1: [this.formAdministrator.streetAddress1, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      streetAddress2: [this.formAdministrator.streetAddress2, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      city: [this.formAdministrator.city, [
        Validators.maxLength(50)
      ]],
      state: [this.formAdministrator.state],
      postalCode: [this.formAdministrator.postalCode, [
        Validators.pattern(AppConstants.OLValidators.PostalCode)
      ]],
      notes: [this.formAdministrator.notes, [
        Validators.maxLength(2000)
      ]]
    });
    this.administratorForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.administratorForm) {
      const form = this.administratorForm;
      this.updateFormErrors(form, this.formErrors, this.validationMessages);
    }
  }

  private updateFormErrors(form: FormGroup, formErrors: any, validationMessages: any): void {
    for (const field in formErrors) {
      const control = form.get(field);
      formErrors[field] = '';
      if (control && control.dirty && !control.valid) {
        const messages = validationMessages[field];
        for (const key in control.errors) {
          formErrors[field] += messages[key] + ' ';
        }
      }
    }
  }

  private setEditing(editing: boolean): void {
    if (this.administratorForm) {
      if (editing) {
        this.administratorForm.enable();
        this.editing = true;
      } else {
        this.administratorForm.disable();
        this.editing = false;
      }
    }
  }

  private getRoles(): void {
    this.roles = Object.keys(AppConstants.Role).map(key => AppConstants.Role[key]);
  }

  private getStates(): void {
    this.states = AppConstants.States;
    this.filteredStates = this.administratorForm.get('state')
      .valueChanges
      .startWith(null)
      .map(val => val ? this.filterStates(val) : this.states.slice());
  }

  private filterStates(val: string): any[] {
    return this.states.filter(state => new RegExp(`${val}`, 'gi').test(state.name));
  }

  save(): void {
    if (this.administratorForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.administratorForm.markAsTouched();
    }
  }

  private add(): void {
    this.userService.create(this.administratorForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added administrator');
    }, error => {
      this.notify.error('Failed to add administrator');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminTabs.Administrator.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated administrator');
    }, error => {
      this.notify.error('Failed to update administrator');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formAdministrator.id,
      firstName: this.administratorForm.get('firstName').value,
      lastName: this.administratorForm.get('lastName').value,
      login: this.administratorForm.get('login').value,
      password: this.administratorForm.get('password').value,
      authority: AppConstants.Role.Admin,
      email: this.administratorForm.get('email').value,
      phoneNumber: this.administratorForm.get('phoneNumber').value,
      streetAddress1: this.administratorForm.get('streetAddress1').value,
      streetAddress2: this.administratorForm.get('streetAddress2').value,
      city: this.administratorForm.get('city').value,
      state: this.administratorForm.get('state').value,
      postalCode: this.administratorForm.get('postalCode').value,
      notes: this.administratorForm.get('notes').value,
    };
  }

  delete(): void {
    this.userService.delete(this.formAdministrator.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formAdministrator.id
        }
      });
      this.notify.success('Successfully deleted administrator');
    }, error => {
      this.notify.error('Failed to delete administrator');
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
