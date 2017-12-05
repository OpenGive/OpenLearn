import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";
import * as _ from "lodash";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AppConstants} from "../../../../app.constants";
import {AdminTabs} from "../../admin.constants";
import {NotifyService} from "../../../../services/notify.service";
import {AdminService} from "../../../../services/admin.service";
import {Principal} from "../../../../shared/auth/principal.service";

@Component({
  selector: 'admin-instructors-form',
  templateUrl: './admin-instructors-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminInstructorsFormComponent implements OnInit {

  @Input('item') formInstructor: any;
  @Input() adding: boolean;
  @Input('organizations') organizations: any[];
  editing: boolean;
  changingPassword: boolean;
  isInstructor: boolean;

  roles: string[];
  states: any[];

  filteredStates: Observable<any[]>;

  instructorForm: FormGroup;
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
    postalCode: '',
    orgRole: ''
  };
  validationMessages = {
    firstName: {
      required: 'First Name is required',
      maxlength: 'First Name cannot be more than 50 characters long'
    },
    lastName: {
      required: 'Last Name is required',
      maxlength: 'Last Name cannot be more than 50 characters long'
    },
    login: {
      required: 'Username is required',
      pattern: 'Username contains invalid characters',
      maxlength: 'Username cannot be more than 100 characters long'
    },
    password: {
      required: 'Password is required',
      pattern: 'Password must be between 8 and 100 characters and contain at least one letter, one digit, and one of !@#$%^&*()_+'
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
    },
    orgRole: {
      required: 'Org Role is required'
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              private notify: NotifyService,
              private adminService: AdminService,
              private principal: Principal) {}

  ngOnInit(): void {
    this.isInstructor = this.principal.getRole() == AppConstants.Role.Instructor.name;
    this.buildForm();
    this.setEditing(this.adding);
    this.resetPassword(false);
    this.getRoles();
    this.getStates();
  }

  private buildForm(): void {
    this.instructorForm = this.fb.group({
      firstName: [this.formInstructor.firstName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      lastName: [this.formInstructor.lastName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      login: [this.formInstructor.login, [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Login),
        Validators.maxLength(50)
      ]],
      password: [this.formInstructor.password, this.adding ? [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Password)
      ] : []],
      authority: [AppConstants.Role.Instructor.name],
      organizationId: [this.formInstructor.organizationId, [
        Validators.required
      ]],
      notes: [this.formInstructor.notes, [
        Validators.maxLength(2000)
      ]],
      email: [this.formInstructor.email, [
        // Validators.email, TODO: This forces email to be required, https://github.com/angular/angular/pull/16902 is the fix, pattern below is the workaround
        Validators.pattern(AppConstants.OLValidators.Email),
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      phoneNumber: [this.formInstructor.phoneNumber, [
        // TODO: Pattern
        Validators.maxLength(15)
      ]],
      streetAddress1: [this.formInstructor.streetAddress1, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      streetAddress2: [this.formInstructor.streetAddress2, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      city: [this.formInstructor.city, [
        Validators.maxLength(50)
      ]],
      state: [this.formInstructor.state],
      postalCode: [this.formInstructor.postalCode, [
        Validators.pattern(AppConstants.OLValidators.PostalCode)
      ]],
      orgRole: [this.formInstructor.orgRole, [
        Validators.required
      ]]
    });
    this.instructorForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.instructorForm) {
      const form = this.instructorForm;
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
    if (this.instructorForm) {
      if (editing) {
        this.instructorForm.enable();
        this.editing = true;
      } else {
        this.instructorForm.disable();
        this.editing = false;
      }
    }
  }

  private getRoles(): void {
    this.roles = Object.keys(AppConstants.Role).map(key => AppConstants.Role[key]);
  }

  private getStates(): void {
    this.states = AppConstants.States;
    this.filteredStates = this.instructorForm.get('state')
      .valueChanges
      .startWith(null)
      .map(val => val ? this.filterStates(val) : this.states.slice());
  }

  private filterStates(val: string): any[] {
    return this.states.filter(state => new RegExp(`${val}`, 'gi').test(state.name));
  }

  save(): void {
    if (this.instructorForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.instructorForm.markAsTouched();
    }
  }

  private add(): void {
    this.adminService.create(AdminTabs.Instructor.route, this.instructorForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added instructor');
    }, error => {
      this.notify.error('Failed to add instructor');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminTabs.Instructor.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated instructor');
    }, error => {
      this.notify.error('Failed to update instructor');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formInstructor.id,
      firstName: this.instructorForm.get('firstName').value,
      lastName: this.instructorForm.get('lastName').value,
      login: this.instructorForm.get('login').value,
      password: this.instructorForm.get('password').value,
      authority: AppConstants.Role.Instructor.name,
      notes: this.instructorForm.get('notes').value,
      email: this.instructorForm.get('email').value,
      phoneNumber: this.instructorForm.get('phoneNumber').value,
      streetAddress1: this.instructorForm.get('streetAddress1').value,
      streetAddress2: this.instructorForm.get('streetAddress2').value,
      city: this.instructorForm.get('city').value,
      state: this.instructorForm.get('state').value,
      postalCode: this.instructorForm.get('postalCode').value,
      organizationId: this.instructorForm.get('organizationId').value,
      orgRole: this.instructorForm.get('orgRole').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminTabs.Instructor.route, this.formInstructor.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formInstructor.id
        }
      });
      this.notify.success('Successfully deleted instructor');
    }, error => {
      this.notify.error('Failed to delete instructor');
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
    if (changingPassword) {
      this.instructorForm.controls.password.setValidators([
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Password)
      ]);
    }
  }

  displayState(stateValue: string): string {
    return stateValue ? _.filter(AppConstants.States, {value: stateValue})[0].name : '';
  }
}
