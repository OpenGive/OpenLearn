import {Component, Input, OnInit} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {MdDialogRef} from "@angular/material";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AppConstants} from "../../../../app.constants";
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

  canEdit: boolean;
  canDelete: boolean;

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private principal: Principal) {
    this.instructorForm = new FormGroup({});
  }

  ngOnInit(): void {
    this.editing = this.adding;
    this.canDelete = this.principal.getRole() === AppConstants.Role.Admin.name ||
                     this.principal.getRole() === AppConstants.Role.OrgAdmin.name;
    this.canEdit = this.canDelete || this.principal.getId() === this.formInstructor.id;
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
        id: this.formInstructor.id
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }

}
