import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";
import * as _ from "lodash";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AppConstants} from "../../../../app.constants";
import {AdminTabs} from "../../admin.constants";
import {NotifyService} from "../../../../services/notify.service";
import {AdminService} from "../../../../services/admin.service";
import {Principal} from "../../../../shared/auth/principal-storage.service";

@Component({
  selector: 'admin-instructors-dialog',
  templateUrl: './admin-instructors-dialog.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminInstructorsDialogComponent implements OnInit {

  @Input('item') formInstructor: any;
  @Input() adding: boolean;
  @Input('organizations') organizations: any[];

  editing: boolean;
  instructorForm: FormGroup;

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>) {
    this.instructorForm = new FormGroup({});
  }

  ngOnInit(): void {
    this.editing = this.adding;
  }
  // TODO: Convert this to a 'status' of EDITING, ADDING, VIEWING, etc
  updateEditing(isEditing: boolean): void {
    this.editing = isEditing;
  };

  added(account: Account): void {
    this.dialogRef.close({
      type: 'ADD',
      data: account
    });
  };

  updated(account: Account): void {
    this.dialogRef.close({
      type: 'UPDATE',
      data: account
    });
  }

  deleted(): void {
    this.dialogRef.close({
      type: 'DELETE',
      data: {
        id: this.formInstructor.id
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }

}
