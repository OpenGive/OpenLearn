import {Component, Input, OnInit} from "@angular/core";
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

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>) {}

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