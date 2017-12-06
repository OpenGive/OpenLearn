import {Component, OnInit, Inject, Input} from '@angular/core';

import {AdminTabs} from "../../controls/admin/admin.constants";
import {AdminService} from "../../services/admin.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotifyService} from "../../services/notify.service";
import {Observable} from "rxjs/Observable";
import {Router, ActivatedRoute} from "@angular/router";
import {Principal} from "../../shared/auth/principal.service";
import {AppConstants} from "../../app.constants";
import {User} from "../../models/user.model";
import {Student} from "../../models/student.model";
import {FourteenPlusValidator} from "../../controls/admin/custom.validators"
import * as _ from "lodash";

@Component({
  selector: 'app-student-page',
  templateUrl: './student-page.component.html',
  styleUrls: ['./student-page.component.css']
})
export class StudentPageComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private fb: FormBuilder,
              private adminService: AdminService,
              private notify: NotifyService,
              private router: Router,
              private principle: Principal) {}

  private adding:boolean = false;
  private editing:boolean = false;
  private studentView:boolean = false;
  private changingPassword:boolean;

  states: any[];
  filteredStates: Observable<any[]>;
  gradeLevels: any[];
  private roles: any[];

  organizations:         any[];
  filteredOrganizations: Observable<any[]>;
  filteredGradeLevels:   Observable<any[]>;

  filteredInstructors: Observable<any[]>;
  filteredSessions: Observable<any[]>;

  private studentId: String;
  private student: Student;
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

  ngOnInit(): void {
    this.studentView = this.principle.getRole() === AppConstants.Role.Student.name;

    this.route.data.subscribe((data: {student: Student}) => {
      this.student = data.student;
    });

    this.studentId = this.student.id;
    console.log(this.student);
    this.buildForm();
    this.setEditing(this.adding);
    this.resetPassword(false);
    this.getStates();
    this.getGradeLevels();
    this.getOrganizations();
    if(!this.studentView) {
    }
  }

  private buildForm(): void {
    this.studentForm = this.fb.group({
      firstName: [this.student.firstName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      lastName: [this.student.lastName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      login: [this.student.login, [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Login),
        Validators.maxLength(50)
      ]],
      password: [this.student.password, []],
      email: [this.student.email, [
        // Validators.email, TODO: This forces email to be required, https://github.com/angular/angular/pull/16902 is the fix, pattern below is the workaround
        Validators.pattern(AppConstants.OLValidators.Email),
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      phoneNumber: [this.student.phoneNumber, [
        // TODO: Pattern
        Validators.maxLength(15)
      ]],
      streetAddress1: [this.student.streetAddress1, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      streetAddress2: [this.student.streetAddress2, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      city: [this.student.city, [
        Validators.maxLength(50)
      ]],
      state: [this.student.state],
      postalCode: [this.student.postalCode, [
        Validators.pattern(AppConstants.OLValidators.PostalCode)
      ]],
      notes: [this.student.notes, [
        Validators.maxLength(50)
      ]],
      fourteenPlus: [this.student.fourteenPlus || false],
      organizationId: [this.student.organizationId, [
        Validators.required
      ]],
      guardianFirstName: [this.student.guardianFirstName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      guardianLastName: [this.student.guardianLastName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      guardianEmail: [this.student.guardianEmail, [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Email),
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      guardianPhone: [this.student.guardianPhone, [
        Validators.required,
        // TODO: Pattern
        Validators.maxLength(15)
      ]],
      school: [this.student.school, [
        Validators.required,
        Validators.maxLength(100)
      ]],
      gradeLevel: [this.student.gradeLevel, [
        Validators.required,
        Validators.maxLength(100)
      ]],
      stateStudentId: [this.student.stateStudentId, [
        Validators.maxLength(100)
      ]],
      orgStudentId: [this.student.orgStudentId, [
        Validators.maxLength(100)
      ]]}, { validator: FourteenPlusValidator('email', 'fourteenPlus')
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

  private getOrganizations(): void {
    this.adminService.getAll(AdminTabs.Organization.route).subscribe(resp => {
      this.organizations = resp;
      this.filteredOrganizations = this.studentForm.get('organizationId')
      .valueChanges
      .startWith(null)
      .map(val => val ? this.filterOrganizations(val) : this.organizations.slice());
    });
  }

  private filterStates(val: string): any[] {
    return this.states.filter(state => new RegExp(`${val}`, 'gi').test(state.name));
  }

  private filterGradeLevels(val: string): any[] {
    return this.gradeLevels.filter(gradeLevel => new RegExp(`${val}`, 'gi').test(gradeLevel.name));
  }

  private filterOrganizations(val: string): any[] {
    return this.organizations.filter(organization => new RegExp(`${val}`, 'gi').test(organization.name));
  }

  save(): void {
    if (this.studentForm.valid) {
      if (this.adding) {

      } else {
        this.update();
      }
    } else {
      this.studentForm.markAsTouched();
    }
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminTabs.Student.route, toUpdate).subscribe(resp => {
      this.setEditing(this.adding);
      this.resetPassword(false);
      this.notify.success('Successfully updated student');
    }, error => {
      this.notify.error('Failed to update student');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.studentId,
      firstName: this.studentForm.get('firstName').value,
      lastName: this.studentForm.get('lastName').value,
      login: this.studentForm.get('login').value,
      password: this.studentForm.get('password').value,
      authority: AppConstants.Role.Student.name,
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
    this.adminService.delete(AdminTabs.Student.route, Number(this.studentId)).subscribe(resp => {
      this.notify.success('Successfully deleted student');
      this.router.navigate(['/admin/students']);
    }, error => {
      this.notify.error('Failed to delete student');
    });
  }

  edit(): void {
    this.setEditing(true);
  }

  cancel(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.resetPassword(false);
  }

  close(): void {

  }

  resetPassword(changingPassword: boolean): void {
    this.changingPassword = changingPassword;
    if (changingPassword) {
      this.studentForm.controls.password.setValidators([
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Password)
      ]);
    } else {
      this.studentForm.controls.password.clearValidators();
    }
  }

  displayState(stateValue: string): string {
    return stateValue ? _.filter(AppConstants.States, {value: stateValue})[0].name : '';
  }

  displayGradeLevel(gradeLevelValue: string): string {
    return gradeLevelValue ? _.filter(AppConstants.GradeLevels, {value: gradeLevelValue})[0].name : '';
  }

  displayInstructor(instructor: any): string {
    return instructor ? instructor.lastName + ', ' + instructor.firstName : '';
  }

  displaySession(session: any): string {
    return session ? session.name : '';
  }
}
