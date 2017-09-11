import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";
import * as _ from "lodash";

import {AppConstants} from "../../../app.constants";
import {NotifyService} from "../../../services/notify.service";
import {UserService} from "../../../services/user.service";
import {CourseStudentDialogComponent} from "../course-student-dialog.component";

@Component({
  selector: 'course-student-form',
  templateUrl: './course-student-form.component.html',
  styleUrls: ['../../dialog-forms.css']
})
export class CourseStudentFormComponent implements OnInit {

  @Input('item') formStudent: any;
  @Input() adding: boolean;
  editing: boolean;

  roles: string[];
  states: any[];

  filteredStates: Observable<any[]>;

  studentForm: FormGroup;
  formErrors = {
    firstName: '',
    lastName: '',
    login: '',
    password: '',
    authorities: '',
    biography: '',
    email: '',
    phoneNumber: '',
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
      required: 'Student must have at least 1 role'
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

  constructor(public dialogRef: MdDialogRef<CourseStudentDialogComponent>,
              private fb: FormBuilder,
              private userService: UserService,
              private notify: NotifyService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.getRoles();
    this.getStates();
  }

  private buildForm(): void {
    this.studentForm = this.fb.group({
      firstName: [this.formStudent.firstName, [
        Validators.maxLength(50)
      ]],
      lastName: [this.formStudent.lastName, [
        Validators.maxLength(50)
      ]],
      login: [this.formStudent.login, [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Login),
        Validators.maxLength(50)
      ]],
      password: [this.formStudent.password, this.adding ? [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(50)
      ] : []],
      authorities: [this.formStudent.authorities, [
        Validators.required
      ]],
      biography: [this.formStudent.biography, [
        Validators.maxLength(2000)
      ]],
      email: [this.formStudent.email, [
        // Validators.email, TODO: This forces email to be required, https://github.com/angular/angular/pull/16902 is the fix, pattern below is the workaround
        Validators.pattern(AppConstants.OLValidators.Email),
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      phoneNumber: [this.formStudent.phoneNumber, [
        // TODO: Pattern
        Validators.maxLength(15)
      ]],
      streetAddress1: [this.formStudent.streetAddress1, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      streetAddress2: [this.formStudent.streetAddress2, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      city: [this.formStudent.city, [
        Validators.maxLength(50)
      ]],
      state: [this.formStudent.state],
      postalCode: [this.formStudent.postalCode, [
        Validators.pattern(AppConstants.OLValidators.PostalCode)
      ]],
      imageUrl: [this.formStudent.imageUrl],
      activated: [this.formStudent.activated || false],
      is14Plus: [this.formStudent.is14Plus || false]
    });
    this.studentForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.studentForm) {
      const form = this.studentForm;
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
    if (this.studentForm) {
      if (editing) {
        this.studentForm.enable();
        this.editing = true;
      } else {
        this.studentForm.disable();
        this.editing = false;
      }
    }
  }

  private getRoles(): void {
    this.roles = Object.keys(AppConstants.Role).map(key => AppConstants.Role[key]);
  }

  private getStates(): void {
    this.states = AppConstants.States;
    this.filteredStates = this.studentForm.get('address').get('state')
      .valueChanges
      .startWith(null)
      .map(val => val ? this.filterStates(val) : this.states.slice());
  }

  private filterStates(val: string): any[] {
    return this.states.filter(state => new RegExp(`${val}`, 'gi').test(state.name));
  }

  save(): void {
    if (this.studentForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.studentForm.markAsTouched();
    }
  }

  private add(): void {
    this.userService.create(this.studentForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added student');
    }, error => {
      this.notify.error('Failed to add student');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.userService.update(toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated student');
    }, error => {
      this.notify.error('Failed to update student');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formStudent.id,
      firstName: this.studentForm.get('firstName').value,
      lastName: this.studentForm.get('lastName').value,
      login: this.studentForm.get('login').value,
      password: this.studentForm.get('password').value,
      authorities: this.studentForm.get('authorities').value,
      biography: this.studentForm.get('biography').value,
      email: this.studentForm.get('email').value,
      phoneNumber: this.studentForm.get('phoneNumber').value,
      streetAddress1: this.studentForm.get('streetAddress1').value,
      streetAddress2: this.studentForm.get('streetAddress2').value,
      city: this.studentForm.get('city').value,
      state: this.studentForm.get('state').value,
      postalCode: this.studentForm.get('postalCode').value,
      imageUrl: this.studentForm.get('imageUrl').value,
      activated: this.studentForm.get('activated').value,
      is14Plus: this.studentForm.get('is14Plus').value
    };
  }

  delete(): void {
    this.userService.delete(this.formStudent.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formStudent.id
        }
      });
      this.notify.success('Successfully deleted student');
    }, error => {
      this.notify.error('Failed to delete student');
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
}
