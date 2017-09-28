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
import {AdminTabs} from "../../admin.constants";

@Component({
  selector: 'admin-students-form',
  templateUrl: './admin-students-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminStudentsFormComponent implements OnInit {

  @Input('item') formStudent: any;
  @Input() adding: boolean;
  editing: boolean;
  changingPassword: boolean;

  roles: string[];
  states: any[];
  gradeLevels: any[];

  filteredStates: Observable<any[]>;
  filteredGradeLevels: Observable<any[]>;

  studentForm: FormGroup;
  formErrors = {
    firstName: '',
    lastName: '',
    login: '',
    password: '',
    notes: '',
    email: '',
    phoneNumber: '',
    streetAddress1: '',
    streetAddress2: '',
    city: '',
    postalCode: '',
    organizationId:'',
    guardianFirstName: '',
    guardianLastName: '',
    guardianEmail: '',
    guardianPhone: '',
    school: '',
    gradeLevel: '',
    stateStudentId: '',
    orgStudentId: ''
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
    organizationId: {
      required: 'Organization is required'
    },
    guardianFirstName: {
      required: 'Guardian First Name is required',
      maxlength: 'Guardian First Name cannot be more than 50 characters long'
    },
    guardianLastName: {
      required: 'Guardian Last Name is required',
      maxlength: 'Guardian Last Name cannot be more than 50 characters long'
    },
    guardianEmail: {
      required: 'Guardian Last Name is required',
      pattern: 'Guardian Email is not formatted correctly',
      minlength: 'Guardian Email must be at least 5 characters long',
      maxlength: 'Guardian Email cannot be more than 100 characters long'
    },
    guardianPhone: {
      required: 'Guardian Phone is required',
      pattern: 'Guardian Phone is not formatted correctly',
      maxlength: 'Phone cannot be more than 15 characters long'
    },
    school: {
      required: 'School is required',
      maxlength: 'School cannot be more than 100 characters long'
    },
    gradeLevel: {
      required: 'Grade Level is required',
      maxlength: 'Grade Level cannot be more than 100 characters long'
    },
    stateStudentId: {
      maxlength: 'State Student ID cannot be more than 100 characters long'
    },
    orgStudentId: {
      maxlength: 'Org Student ID cannot be more than 100 characters long'
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
    this.getGradeLevels();
  }

  private buildForm(): void {
    this.studentForm = this.fb.group({
      firstName: [this.formStudent.firstName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      lastName: [this.formStudent.lastName, [
        Validators.required,
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
      notes: [this.formStudent.notes, [
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
      fourteenPlus: [this.formStudent.fourteenPlus || false],
      authority: [AppConstants.Role.Student],
      organizationId: [this.formStudent.organizationId, [
        Validators.required
      ]],
      guardianFirstName: [this.formStudent.guardianFirstName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      guardianLastName: [this.formStudent.guardianLastName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      guardianEmail: [this.formStudent.guardianEmail, [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Email),
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      guardianPhone: [this.formStudent.guardianPhone, [
        Validators.required,
        // TODO: Pattern
        Validators.maxLength(15)
      ]],
      school: [this.formStudent.school, [
        Validators.required,
        Validators.maxLength(100)
      ]],
      gradeLevel: [this.formStudent.gradeLevel, [
        Validators.required,
        Validators.maxLength(100)
      ]],
      stateStudentId: [this.formStudent.stateStudentId, [
        Validators.maxLength(100)
      ]],
      orgStudentId: [this.formStudent.orgStudentId, [
        Validators.maxLength(100)
      ]]
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
    this.filteredStates = this.studentForm.get('state')
      .valueChanges
      .startWith(null)
      .map(val => val ? this.filterStates(val) : this.states.slice());
  }

  private getGradeLevels(): void {
    this.gradeLevels = AppConstants.GradeLevels;
    this.filteredGradeLevels = this.studentForm.get('gradeLevel')
      .valueChanges
      .startWith(null)
      .map(val => val ? this.filterGradeLevels(val) : this.gradeLevels.slice());
  }

  private filterStates(val: string): any[] {
    return this.states.filter(state => new RegExp(`${val}`, 'gi').test(state.name));
  }

  private filterGradeLevels(val: string): any[] {
    return this.gradeLevels.filter(gradeLevel => new RegExp(`${val}`, 'gi').test(gradeLevel.name));
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
    this.adminService.create(AdminTabs.Student.route, this.studentForm.value).subscribe(resp => {
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
    this.adminService.update(AdminTabs.Student.route, toUpdate).subscribe(resp => {
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
      authority: AppConstants.Role.Student,
      notes: this.studentForm.get('notes').value,
      email: this.studentForm.get('email').value,
      phoneNumber: this.studentForm.get('phoneNumber').value,
      streetAddress1: this.studentForm.get('streetAddress1').value,
      streetAddress2: this.studentForm.get('streetAddress2').value,
      city: this.studentForm.get('city').value,
      state: this.studentForm.get('state').value,
      postalCode: this.studentForm.get('postalCode').value,
      fourteenPlus: this.studentForm.get('fourteenPlus').value,
      organizationId: this.studentForm.get('organizationId').value,
      guardianFirstName: this.studentForm.get('guardianFirstName').value,
      guardianLastName: this.studentForm.get('guardianLastName').value,
      guardianEmail: this.studentForm.get('guardianEmail').value,
      guardianPhone: this.studentForm.get('guardianPhone').value,
      school: this.studentForm.get('school').value,
      gradeLevel: this.studentForm.get('gradeLevel').value,
      stateStudentId: this.studentForm.get('stateStudentId').value,
      orgStudentId: this.studentForm.get('orgStudentId').value
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

  resetPassword(changingPassword: boolean): void {
    this.changingPassword = changingPassword;
  }

  displayState(stateValue: string): string {
    return stateValue ? _.filter(AppConstants.States, {value: stateValue})[0].name : '';
  }

  displayGradeLevel(gradeLevelValue: string): string {
    return gradeLevelValue ? _.filter(AppConstants.GradeLevels, {value: gradeLevelValue})[0].name : '';
  }
}
