import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {Admin} from "../../../../models/admin.model";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'admin-administrators-dialog',
  templateUrl: './admin-administrators-dialog.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminAdministratorsDialogComponent implements OnInit {

  @Input('item') formAdministrator: Admin;
  @Input() adding: boolean;
  @Input('organizations') organizations: any[];

  editing: boolean = false;
  administratorForm: FormGroup;

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>) {
    this.administratorForm = new FormGroup({});
  }

  ngOnInit(): void {
    this.editing = this.adding;
  }

  // TODO: Convert this to a 'status' of EDITING, ADDING, VIEWING, etc
  updateEditing(isEditing: boolean): void {
    console.log('updateEditing', isEditing);
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
        id: this.formAdministrator.id
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }

}