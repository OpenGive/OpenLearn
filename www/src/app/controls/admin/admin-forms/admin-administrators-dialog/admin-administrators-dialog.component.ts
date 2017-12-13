import {Component, Input} from "@angular/core";
import {MdDialogRef} from "@angular/material";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {Admin} from "../../../../models/admin.model";

@Component({
  selector: 'admin-administrators-dialog',
  templateUrl: './admin-administrators-dialog.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminAdministratorsDialogComponent {

  @Input('item') formAdministrator: Admin;
  @Input() adding: boolean;
  @Input('organizations') organizations: any[];

  editing: boolean = false;

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>) {}

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
        id: this.formAdministrator.id
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }

}