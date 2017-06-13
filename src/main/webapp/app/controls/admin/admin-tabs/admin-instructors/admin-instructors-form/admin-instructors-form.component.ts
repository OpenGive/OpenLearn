import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {AppConstants} from "../../../../../app.constants";
import {AdminDialogComponent} from "../../../admin-dialog.component";

@Component({
  selector: 'admin-instructors-form',
  templateUrl: './admin-instructors-form.component.html',
  styleUrls: ['../../admin-forms.css']
})
export class AdminInstructorsFormComponent implements OnInit {

  @Input('item') formInstructor: any;
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
