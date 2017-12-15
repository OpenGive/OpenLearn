import {Component, Input, OnInit} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {MdDialogRef} from "@angular/material";

import {AdminDialogComponent} from "../../admin-dialog.component";

@Component({
  selector: 'admin-org-administrators-dialog',
  templateUrl: './admin-org-administrators-dialog.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminOrgAdministratorsDialogComponent implements OnInit {

  @Input('item') formOrgAdministrator: any;
  @Input() adding: boolean;
  @Input('organizations') organizations: any[];

  editing: boolean;
  orgAdministratorForm: FormGroup;

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>) {
    this.orgAdministratorForm = new FormGroup({});
  }

  ngOnInit(): void {
    this.editing = this.adding;
  }

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
        id: this.formOrgAdministrator.id
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }

}
