import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";
import * as _ from "lodash";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AdminModel} from "../../admin.constants";
import {AdminService} from "../../../../services/admin.service";
import {AppConstants} from "../../../../app.constants";
import {NotifyService} from "../../../../services/notify.service";
import {PasswordService} from "../../../../shared/auth/password.service";
import {UserService} from "../../../../services/user.service";

@Component({
  selector: 'admin-administrators-form',
  templateUrl: './admin-administrators-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminAdministratorsFormComponent implements OnInit {

  @Input('item') formAdministrator: any;
  @Input() adding: boolean;
  editing: boolean;

  roles: string[];
  states: any[];
  organizations: any[];

  filteredStates: Observable<any[]>;
  filteredOrganizations: Observable<any[]>;

  administratorForm: FormGroup;
  formErrors = {
    firstName: '',
    lastName: '',
    login: '',
    password: '',
    authorities: '',
    biography: '',
    email: '',
    phoneNumber: '',
    organization: '',
    address: {
      streetAddress1: '',
      streetAddress2: '',
      city: '',
      postalCode: ''
    }
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
    authorities: {
      required: 'Administrator must have at least 1 role'
    },
    biography: {
      maxlength: 'Biography cannot be more than 2000 characters long'
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
    organization: {
      required: 'Organization is required'
    },
    address: {
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
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              private userService: UserService,
              private notify: NotifyService,
              private passwordService: PasswordService,
              private adminService: AdminService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.getRoles();
    this.getStates();
    this.getOrganizations();
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
      authorities: [this.formAdministrator.authorities, [
        Validators.required
      ]],
      organization: [this.formAdministrator.organization, [
        Validators.required
      ]],
      biography: [this.formAdministrator.biography, [
        Validators.maxLength(2000)
      ]],
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
      address: this.fb.group({
        streetAddress1: [this.formAdministrator.address ? this.formAdministrator.address.streetAddress1 : null, [
          Validators.minLength(5),
          Validators.maxLength(50)
        ]],
        streetAddress2: [this.formAdministrator.address ? this.formAdministrator.address.streetAddress2 : null, [
          Validators.minLength(5),
          Validators.maxLength(50)
        ]],
        city: [this.formAdministrator.address ? this.formAdministrator.address.city : null, [
          Validators.maxLength(50)
        ]],
        state: [this.formAdministrator.address ? this.formAdministrator.address.state : null],
        postalCode: [this.formAdministrator.address ? this.formAdministrator.address.postalCode : null, [
          Validators.pattern(AppConstants.OLValidators.PostalCode)
        ]]
      }),
      imageUrl: [this.formAdministrator.imageUrl],
      activated: [this.formAdministrator.activated || false],
      is14Plus: [this.formAdministrator.is14Plus || false]
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
    this.filteredStates = this.administratorForm.get('address').get('state')
      .valueChanges
      .startWith(null)
      .map(val => val ? this.filterStates(val) : this.states.slice());
  }

  private filterStates(val: string): any[] {
    return this.states.filter(state => new RegExp(`${val}`, 'gi').test(state.name));
  }

  private setOrganizationID(): void {
    console.log("in set org id");
    if (this.administratorForm.valid) {
      console.log("form is valid");
      if (this.administratorForm.get('organization').value != null) {
        console.log("not null value");
        this.administratorForm.get('organization').setValue([this.administratorForm.get('organization').value['id']])
      }
    }
  }

  save(): void {
    if (this.administratorForm.valid) {
      this.setOrganizationID();
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
    this.userService.update(toUpdate).subscribe(resp => {
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
      authorities: this.administratorForm.get('authorities').value,
      biography: this.administratorForm.get('biography').value,
      email: this.administratorForm.get('email').value,
      phoneNumber: this.administratorForm.get('phoneNumber').value,
      address: {
        id: this.formAdministrator.address ? this.formAdministrator.address.id : null,
        streetAddress1: this.administratorForm.get('address').get('streetAddress1').value,
        streetAddress2: this.administratorForm.get('address').get('streetAddress2').value,
        city: this.administratorForm.get('address').get('city').value,
        state: this.administratorForm.get('address').get('state').value,
        postalCode: this.administratorForm.get('address').get('postalCode').value
      },
      imageUrl: this.administratorForm.get('imageUrl').value,
      organizationIds: this.administratorForm.get('organization').value,
      activated: this.administratorForm.get('activated').value,
      is14Plus: this.administratorForm.get('is14Plus').value
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

  resetPassword(): void {
    this.passwordService.resetPassword(this.formAdministrator.login).subscribe(resp => {
      this.notify.success('Password reset email sent');
    }, error => {
      this.notify.error('Failed to send password reset email');
    });
  }

  displayRole(role: string): string { // Convert "ROLE_ONE_TWO" to "One Two"
    return role.split('_').slice(1).map(str => str.charAt(0) + str.slice(1).toLowerCase()).join(' ');
  }

  displayState(stateValue: string): string {
    return stateValue ? _.filter(AppConstants.States, {value: stateValue})[0].name : '';
  }

  displayOrganization(organization: any): string {
    return organization ? organization.name : '';
  }

  private getOrganizations(): void {
    this.adminService.getAll(AdminModel.Organization.route).subscribe(resp => {
      this.organizations = resp;
      this.filteredOrganizations = this.administratorForm.get('organization')
        .valueChanges
        .startWith(null)
        .map(val => val ? this.filterOrganizations(val) : this.organizations.slice());
    });
  }

  private filterOrganizations(val: string): any[] {
    return this.organizations.filter(organization => new RegExp(`${val}`, 'gi').test(organization.name));
  }
}
