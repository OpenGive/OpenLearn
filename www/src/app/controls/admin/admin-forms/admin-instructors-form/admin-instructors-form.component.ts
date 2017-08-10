import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";
import * as _ from "lodash";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AppConstants} from "../../../../app.constants";
import {AdminModel} from "../../admin.constants";
import {NotifyService} from "../../../../services/notify.service";
import {UserService} from "../../../../services/user.service";
import {AdminService} from "../../../../services/admin.service";

@Component({
  selector: 'admin-instructors-form',
  templateUrl: './admin-instructors-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminInstructorsFormComponent implements OnInit {

  @Input('item') formInstructor: any;
  @Input() adding: boolean;
  editing: boolean;

  roles: string[];
  states: any[];
  organizations: any[];

  filteredStates: Observable<any[]>;
  filteredOrganizations: Observable<any[]>;

  instructorForm: FormGroup;
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
      required: 'Instructor must have at least 1 role'
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
              private adminService: AdminService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.getRoles();
    this.getStates();
    this.getOrganizations();
  }

  private buildForm(): void {
    this.instructorForm = this.fb.group({
      firstName: [this.formInstructor.firstName, [
        Validators.maxLength(50)
      ]],
      lastName: [this.formInstructor.lastName, [
        Validators.maxLength(50)
      ]],
      login: [this.formInstructor.login, [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Login),
        Validators.maxLength(50)
      ]],
      password: [this.formInstructor.password, this.adding ? [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(50)
      ] : []],
      authorities: [this.formInstructor.authorities, [
        Validators.required
      ]],
      organization: [this.formInstructor.organization, [
        Validators.required
      ]],
      biography: [this.formInstructor.biography, [
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
      address: this.fb.group({
        streetAddress1: [this.formInstructor.address ? this.formInstructor.address.streetAddress1 : null, [
          Validators.minLength(5),
          Validators.maxLength(50)
        ]],
        streetAddress2: [this.formInstructor.address ? this.formInstructor.address.streetAddress2 : null, [
          Validators.minLength(5),
          Validators.maxLength(50)
        ]],
        city: [this.formInstructor.address ? this.formInstructor.address.city : null, [
          Validators.maxLength(50)
        ]],
        state: [this.formInstructor.address ? this.formInstructor.address.state : null],
        postalCode: [this.formInstructor.address ? this.formInstructor.address.postalCode : null, [
          Validators.pattern(AppConstants.OLValidators.PostalCode)
        ]]
      }),
      imageUrl: [this.formInstructor.imageUrl],
      activated: [this.formInstructor.activated || false],
      is14Plus: [this.formInstructor.is14Plus || false]
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
    this.filteredStates = this.instructorForm.get('address').get('state')
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
    console.log(this.instructorForm.value);
    this.userService.create(this.instructorForm.value).subscribe(resp => {
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
    this.userService.update(toUpdate).subscribe(resp => {
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
      authorities: this.instructorForm.get('authorities').value,
      biography: this.instructorForm.get('biography').value,
      email: this.instructorForm.get('email').value,
      phoneNumber: this.instructorForm.get('phoneNumber').value,
      address: {
        id: this.formInstructor.address ? this.formInstructor.address.id : null,
        streetAddress1: this.instructorForm.get('address').get('streetAddress1').value,
        streetAddress2: this.instructorForm.get('address').get('streetAddress2').value,
        city: this.instructorForm.get('address').get('city').value,
        state: this.instructorForm.get('address').get('state').value,
        postalCode: this.instructorForm.get('address').get('postalCode').value
      },
      imageUrl: this.instructorForm.get('imageUrl').value,
      organization: this.instructorForm.get('organization').value,
      activated: this.instructorForm.get('activated').value,
      is14Plus: this.instructorForm.get('is14Plus').value
    };
  }

  delete(): void {
    this.userService.delete(this.formInstructor.id).subscribe(resp => {
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
      this.filteredOrganizations = this.instructorForm.get('organization')
        .valueChanges
        .startWith(null)
        .map(val => val ? this.filterOrganizations(val) : this.organizations.slice());
    });
  }

  private filterOrganizations(val: string): any[] {
    return this.organizations.filter(organization => new RegExp(`${val}`, 'gi').test(organization.name));
  }
}
