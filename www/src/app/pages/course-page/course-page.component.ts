import {Component, OnInit} from '@angular/core';

import {AdminModel} from "../../controls/admin/admin.constants";
import {AdminService} from "../../services/admin.service";
import {UserService} from "../../services/user.service";
import {Course} from '../../models/course';
import {DataService} from "../../services/data.service"
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotifyService} from "../../services/notify.service";
import {Observable} from "rxjs/Observable";
import {Router} from "@angular/router";
import {Principal} from "../../shared/auth/principal.service";
import {AppConstants} from "../../app.constants";

@Component({
  selector: 'app-course-page',
  templateUrl: './course-page.component.html',
  styleUrls: ['./course-page.component.css']
})
export class CoursePageComponent implements OnInit {


  constructor(private fb: FormBuilder,
              private adminService: AdminService,
              private userService: UserService,
              private notify: NotifyService,
              private dataService: DataService,
              private router: Router,
              private principal: Principal) {}

  private adding:boolean = false;
  private editing:boolean = false;
  private studentView:boolean = false;

  private instructors: any[];
  private sessions: any[];

  filteredInstructors: Observable<any[]>;
  filteredSessions: Observable<any[]>;

  private course:Course;
  courseForm: FormGroup;
  formErrors = {
    name: '',
    description: '',
    session: '',
    instructor: '',
    startDate: '',
    endDate: ''
  };
  validationMessages = {
    name: {
      required: 'Name is required',
      minlength: 'Name must be at least 3 characters long',
      maxlength: 'Name cannot be more than 100 characters long'
    },
    description: {
      minlength: 'Description must be at least 5 characters long',
      maxlength: 'Description cannot be more than 200 characters long'
    },
    session: {
      required: 'Session is required'
    },
    instructor: {
      required: 'Instructor is required'
    },
    startDate: {
      mdDatepickerMax: 'Start date must not be after End Date'
    },
    endDate: {
      mdDatepickerMin: 'End date must not be before Start Date'
    }
  };

  ngOnInit(): void {
    this.studentView = this.principal.hasAuthority(AppConstants.Role.Student);
    this.course = this.dataService.course;
    if(typeof this.course == "undefined")
    {
      this.router.navigate(['access-denied']);
    }
    this.buildForm();
    this.setEditing(this.adding);
    this.getInstructors();
    if(!this.studentView) {
      this.getSessions();
    }
  }
  private buildForm(): void {
    this.courseForm = this.fb.group({
      name: [this.course.name, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(100)
      ]],
      description: [this.course.description, [
        Validators.minLength(5),
        Validators.maxLength(200)
      ]],
      session: [this.course.session, [
        Validators.required
      ]],
      instructor: [this.course.instructor, [
        Validators.required
      ]],
      startDate: [this.course.startDate],
      endDate: [this.course.endDate]
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
    this.adminService.getAll(AdminModel.Instructor.route).subscribe(resp => {
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

  private getSessions(): void {
    this.adminService.getAll(AdminModel.Session.route).subscribe(resp => {
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

      } else {
        this.update();
      }
    } else {
      this.courseForm.markAsTouched();
    }
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminModel.Course.route, toUpdate).subscribe(resp => {

      this.notify.success('Successfully updated course');
    }, error => {
      this.notify.error('Failed to update course');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.course.id,
      name: this.courseForm.get('name').value,
      description: this.courseForm.get('description').value,
      session: this.courseForm.get('session').value,
      instructor: this.courseForm.get('instructor').value,
      startDate: this.courseForm.get('startDate').value,
      endDate: this.courseForm.get('endDate').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminModel.Course.route, this.course.id).subscribe(resp => {

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

  }

  displayInstructor(instructor: any): string {
    return instructor ? instructor.lastName + ', ' + instructor.firstName : '';
  }

  displaySession(session: any): string {
    return session ? session.name : '';
  }


}
