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
import {AdminTabs} from "../../admin/admin.constants"

@Component({
  selector: 'assignment-form',
  templateUrl: './assignment-form.component.html',
  styleUrls: ['../../dialog-forms.css']
})

export class AssignmentFormComponent implements OnInit {

  formAssignment: any;
  adding: Boolean;
  editing: Boolean = false;

  assignmentForm: FormGroup;

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              @Inject(MD_DIALOG_DATA) public data: any,
              private notify: NotifyService,
              private adminService: AdminService) {}

  ngOnInit(): void {
    this.formAssignment = {};
    this.buildForm();
    this.adding = this.data.adding;
    this.setEditing(this.adding);
    console.log(this.data);
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
    })
    this.assignmentForm.disable();
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
    // const toUpdate = this.prepareToUpdate();
    // this.adminService.update(AdminTabs.Assignment.route, )
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
}
