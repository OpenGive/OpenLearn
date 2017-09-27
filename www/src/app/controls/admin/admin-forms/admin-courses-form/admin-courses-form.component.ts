import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AdminTabs} from "../../admin.constants";
import {AdminService} from "../../../../services/admin.service";
import {NotifyService} from "../../../../services/notify.service";
import {UserService} from "../../../../services/user.service";

@Component({
  selector: 'admin-courses-form',
  templateUrl: './admin-courses-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminCoursesFormComponent implements OnInit {

  @Input('item') formCourse: any;
  @Input() adding: boolean;
  editing: boolean;

  instructors: any[];
  organizations: any[];
  sessions: any[];

  filteredInstructors: Observable<any[]>;
  filteredOrganizations: Observable<any[]>;
  filteredSessions: Observable<any[]>;

  courseForm: FormGroup;
  formErrors = {
    name: '',
    description: '',
    sessionId: '',
    instructorId: '',
    startDate: '',
    endDate: '',
    locations: '',
    times: ''
  };
  validationMessages = {
    name: {
      required: 'Name is required',
      minlength: 'Name must be at least 3 characters long',
      maxlength: 'Name cannot be more than 100 characters long'
    },
    description: {
      required: 'Name is required',
      minlength: 'Description must be at least 5 characters long',
      maxlength: 'Description cannot be more than 200 characters long'
    },
    sessionId: {
      required: 'Session is required'
    },
    instructorId: {
      required: 'Instructor is required'
    },
    startDate: {
      required: 'Name is required',
      mdDatepickerMax: 'Start date must not be after End Date'
    },
    endDate: {
      required: 'Name is required',
      mdDatepickerMin: 'End date must not be before Start Date'
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              private adminService: AdminService,
              private notify: NotifyService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.getInstructors();
    this.getOrganizations();
    this.getSessions();
  }

  private buildForm(): void {
    this.courseForm = this.fb.group({
      name: [this.formCourse.name, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(100)
      ]],
      description: [this.formCourse.description, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(200)
      ]],
      sessionId: [this.formCourse.session, [
        Validators.required
      ]],
      instructorId: [this.formCourse.instructor, [
        Validators.required
      ]],
      startDate: [this.formCourse.startDate, [
        Validators.required
      ]],
      endDate: [this.formCourse.endDate, [
        Validators.required
      ]],
      locations: [this.formCourse.locations],
      times: [this.formCourse.times],
    });
    this.courseForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.courseForm) {
      const form = this.courseForm;
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

  private setEditing(editing: boolean): void {
    if (this.courseForm) {
      if (editing) {
        this.courseForm.enable();
        this.editing = true;
      } else {
        this.courseForm.disable();
        this.editing = false;
      }
    }
  }

  private getInstructors(): void {
    this.adminService.getAll(AdminTabs.Instructor.route).subscribe(resp => {
      this.instructors = resp;
      this.filteredInstructors = this.courseForm.get('instructor')
        .valueChanges
        .startWith(null)
        .map(val => val ? this.filterInstructors(val) : this.instructors.slice());
    });
  }

  private filterInstructors(val: string): any[] {
    return this.instructors.filter(instructor => {
      let input = new RegExp(`${val}`, 'gi');
      return (input.test(instructor.firstName + ' ' + instructor.lastName)
        || input.test(instructor.lastName + ', ' + instructor.firstName));
    });
  }

  private getOrganizations(): void {
    this.adminService.getAll(AdminTabs.Organization.route).subscribe(resp => {
      this.organizations = resp;
      this.filteredOrganizations = this.courseForm.get('organization')
        .valueChanges
        .startWith(null)
        .map(val => val ? this.filterOrganizations(val) : this.organizations.slice());
    });
  }

  private filterOrganizations(val: string): any[] {
    return this.organizations.filter(organization => new RegExp(`${val}`, 'gi').test(organization.name));
  }

  private getSessions(): void {
    this.adminService.getAll(AdminTabs.Session.route).subscribe(resp => {
      this.sessions = resp;
      this.filteredSessions = this.courseForm.get('session')
        .valueChanges
        .startWith(null)
        .map(val => val ? this.filterSessions(val) : this.sessions.slice());
    });
  }

  private filterSessions(val: string): any[] {
    return this.sessions.filter(session => new RegExp(`${val}`, 'gi').test(session.name));
  }

  save(): void {
    if (this.courseForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.courseForm.markAsTouched();
    }
  }

  private add(): void {
    this.adminService.create(AdminTabs.Course.route, this.courseForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added course');
    }, error => {
      this.notify.error('Failed to add course');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminTabs.Course.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated course');
    }, error => {
      this.notify.error('Failed to update course');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formCourse.id,
      name: this.courseForm.get('name').value,
      description: this.courseForm.get('description').value,
      sessionId: this.courseForm.get('sessionId').value,
      instructorId: this.courseForm.get('instructorId').value,
      startDate: this.courseForm.get('startDate').value,
      endDate: this.courseForm.get('endDate').value,
      locations: this.courseForm.get('locations').value,
      times: this.courseForm.get('times').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminTabs.Course.route, this.formCourse.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formCourse.id
        }
      });
      this.notify.success('Successfully deleted course');
    }, error => {
      this.notify.error('Failed to delete course');
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

  displayInstructor(instructor: any): string {
    return instructor ? instructor.lastName + ', ' + instructor.firstName : '';
  }

  displayOrganization(organization: any): string {
    return organization ? organization.name : '';
  }

  displaySession(session: any): string {
    return session ? session.name : '';
  }
}
