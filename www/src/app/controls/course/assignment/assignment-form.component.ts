import {Component, Inject, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MD_DIALOG_DATA, MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";
import * as _ from "lodash";

import {AdminDialogComponent} from "../../admin/admin-dialog.component";
import {AdminService} from "../../../services/admin.service";
import {AppConstants} from "../../../app.constants";
import {NotifyService} from "../../../services/notify.service";
import {UserService} from "../../../services/user.service";
import {OrgAdmin} from "../../../models/org-admin.model";
import {AdminTabs} from "../../admin/admin.constants";
import {StudentCourseService} from "../../../services/student-course.service";

@Component({
  selector: 'assignment-form',
  templateUrl: './assignment-form.component.html',
  styleUrls: ['../../dialog-forms.css']
})

export class AssignmentFormComponent implements OnInit {

  formAssignment: any;
  adding: Boolean;
  editing: Boolean = false;
  students: any[];

  assignmentForm: FormGroup;

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              @Inject(MD_DIALOG_DATA) public data: any,
              private notify: NotifyService,
              private adminService: AdminService,
              private courseService: StudentCourseService) {}

  columns: any[];
  formErrors = {
    name: '',
    description: ''
  };

  validationMessages = {
    name: {
      required: 'Name is required',
      maxlength: 'Name must be less than 50 characters long'
    },
    description: {
      required: 'Description is required',
      maxlength: 'Description must be less than 100 characters long',
      minlength: 'Description must be more than 10 characters long'
    }
  };
  ngOnInit(): void {
    this.columns = [
      {
        id: "firstName",
        name: "FirstName"
      },
      {
        id: "lastName",
        name: "LastName"
      },
      {
        id: "grade",
        name: "Grade"
      }
    ];
    this.formAssignment = this.data.assignment;
    this.getStudents();
    this.buildForm();
    this.adding = this.data.adding;
    this.setEditing(this.adding);
  }

  private buildForm(): void {
    this.assignmentForm = this.fb.group({
      name: [this.formAssignment.name, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      description: [this.formAssignment.description, [
        Validators.maxLength(100),
        Validators.minLength(10)
      ]]
    });
    this.assignmentForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
    this.assignmentForm.disable();
  }

  private onValueChanged(): void {
    if (this.assignmentForm) {
      const form = this.assignmentForm;
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

  save(): void {
    if (this.assignmentForm.valid) {
      if(this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.assignmentForm.markAsTouched();
    }
  }

  private add(): void {
    console.log(this.assignmentForm);
    this.adminService.create(AdminTabs.Assignment.route, {
      name: this.assignmentForm.value.name,
      description: this.assignmentForm.value.description,
      courseId: this.data.course.id
    }).subscribe(resp =>{
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added assignment');
    }, error => {
      this.notify.error('Failed to add assignment');
    })
  }

  private update(): void {
    console.log(this.prepareToUpdate());
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminTabs.Assignment.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated assignment');
    }, error => {
      this.notify.error('Failed to update assignment');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.data.assignment.id,
      name: this.assignmentForm.value.name,
      description: this.assignmentForm.value.description,
      courseId: this.data.course.id
    }
  }
  close(): void {
    this.dialogRef.close();
  }

  edit(): void {
    this.setEditing(true);
  }

  private setEditing(editing: Boolean): void {
    if (this.assignmentForm) {
      if(editing) {
        this.assignmentForm.enable();
        this.editing = true;
      } else {
        this.assignmentForm.disable();
        this.editing = false;
      }
    }
  }

  cancel(): void {
    this.ngOnInit();
  }

  getStudents(): void {
    this.courseService.getStudentCoursesByCourse(this.data.course.id).subscribe(students => {
      this.students = students;
      console.log(this.students);
    })
  }
}
