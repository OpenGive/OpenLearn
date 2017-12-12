import {Component, Inject, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MD_DIALOG_DATA, MdDialog, MdDialogRef} from "@angular/material";
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
import {AssignmentService} from "../../../services/assignment.service";
import {GradeDialogComponent} from "../grade-dialog.component";

@Component({
  selector: 'portfolio-form',
  templateUrl: './portfolio-form.component.html',
  styleUrls: ['../../dialog-forms.css']
})

export class PortfolioFormComponent implements OnInit {

  formPortfolio: any;
  adding: Boolean;
  editing: Boolean = false;
  students: any[];
  studentView: boolean;

  portfolioForm: FormGroup;

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              public dialog: MdDialog,
              private fb: FormBuilder,
              @Inject(MD_DIALOG_DATA) public data: any,
              private notify: NotifyService,
              private adminService: AdminService,
              private courseService: StudentCourseService,
              private assignmentService: AssignmentService) {}

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
    this.studentView = this.data.studentView;
    this.formPortfolio = this.data.portfolioItem;
    this.buildForm();
    this.adding = this.data.adding;
    this.setEditing(this.adding);
  }

  private buildForm(): void {
    this.portfolioForm = this.fb.group({
      name: [this.formPortfolio.name, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      description: [this.formPortfolio.description, [
        Validators.maxLength(100),
        Validators.minLength(10)
      ]]
    });
    this.portfolioForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
    this.portfolioForm.disable();
  }

  private onValueChanged(): void {
    if (this.portfolioForm) {
      const form = this.portfolioForm;
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
    if (this.portfolioForm.valid) {
      if(this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.portfolioForm.markAsTouched();
    }
  }

  private add(): void {
    console.log(this.portfolioForm);
    this.adminService.create(AdminTabs.Portfolio.route, {
      name: this.portfolioForm.value.name,
      description: this.portfolioForm.value.description,
      studentId: this.data.student.id
    }).subscribe(resp =>{
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added portfolio item');
    }, error => {
      this.notify.error('Failed to add portfolio item');
    })
  }

  private update(): void {
    console.log(this.prepareToUpdate());
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminTabs.Portfolio.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated portfolio item');
    }, error => {
      this.notify.error('Failed to update portfolio item');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.data.portfolioItem.id,
      name: this.portfolioForm.value.name,
      description: this.portfolioForm.value.description,
      studentId: this.data.student.id
    }
  }

  delete(): void {
    this.adminService.delete(AdminTabs.Portfolio.route, this.data.portfolioItem.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: resp
      });
      this.notify.success('Successfully deleted portfolio item');
    }, error => {
      this.notify.error('Failed to delete portfolio item');
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  edit(): void {
    this.setEditing(true);
  }

  private setEditing(editing: Boolean): void {
    if (this.portfolioForm) {
      if(editing) {
        this.portfolioForm.enable();
        this.editing = true;
      } else {
        this.portfolioForm.disable();
        this.editing = false;
      }
    }
  }

  cancel(): void {
    this.ngOnInit();
  }


  stopPropagation(e): void {
    e.stopPropagation();
  }
}
