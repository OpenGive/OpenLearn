import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {AppConstants} from "../../../../../app.constants";
import {AdminDialogComponent} from "../../../admin-dialog.component";

@Component({
  selector: 'admin-students-form',
  templateUrl: './admin-students-form.component.html',
  styleUrls: ['../../admin-forms.css']
})
export class AdminStudentsFormComponent implements OnInit {

  @Input('item') formStudent: any;
  @Input() editing: boolean;
  @Input() adding: boolean;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>) {}

  ngOnInit() {
    this.getRoles();
  }

  getRoles(): void {
    this.roles = Object.keys(AppConstants.Role).map(key => AppConstants.Role[key]);
  }
}
