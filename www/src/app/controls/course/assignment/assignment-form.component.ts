import {Component, Inject, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MD_DIALOG_DATA, MdDialog, MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";
import * as _ from "lodash";

import {AdminDialogComponent} from "../../admin/admin-dialog.component";
import {AdminService} from "../../../services/admin.service";
import {AppConstants} from "../../../app.constants";
import {NotifyService} from "../../../services/notify.service";
import {OrgAdmin} from "../../../models/org-admin.model";
import {AdminTabs} from "../../admin/admin.constants";
import {StudentCourseService} from "../../../services/student-course.service";
import {AssignmentService} from "../../../services/assignment.service";
import {Principal} from "../../../shared/auth/principal-storage.service";
import {GradeDialogComponent} from "../grade-dialog.component";

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
  studentView: boolean;
  instructorCheck: boolean = true;

  assignmentForm: FormGroup;

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              public dialog: MdDialog,
              private fb: FormBuilder,
              @Inject(MD_DIALOG_DATA) public data: any,
              private notify: NotifyService,
              private adminService: AdminService,
              private courseService: StudentCourseService,
              private assignmentService: AssignmentService,
              private principal: Principal) {}

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
    this.studentView = this.data.studentView;
    this.formAssignment = this.data.assignment;
    this.getStudents();
    this.buildForm();
    this.adding = this.data.adding;
    this.setEditing(this.adding);
    if (this.principal.hasAuthority(AppConstants.Role.Instructor.name)) this.instructorCheck = this.data.course.instructorId == this.principal.getId();
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

  delete(): void {
    if (this.data.assignment !== undefined && this.data.assignment.hasOwnProperty("id")) {
      this.adminService.delete(AdminTabs.Assignment.route, this.data.assignment.id).subscribe(resp => {
        this.dialogRef.close({
          type: 'DELETE',
          data: resp
        });
        this.notify.success('Successfully deleted assignment');
      }, error => {
        this.notify.error('Failed to delete assignment');
      });
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

  editGrade(assignment, e): void {
    console.log(assignment);
    this.stopPropagation(e);
    this.dialog.open(GradeDialogComponent, {
      data: {
        assignment: assignment,
        type: 'ASSIGNMENT'
      },
      width: "50px",
      height: "200px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
      //this.handleEditGradeResponse(resp)
    });
  }

  getStudents(): void {
    if (this.studentView) return;

    this.assignmentService.getAssignmentStudentByAssignmentId(this.data.assignment.id).subscribe(assignmentStudents => {
      this.students = assignmentStudents;
      console.log(this.students)
    })

  }

  stopPropagation(e): void {
    e.stopPropagation();
  }
}
