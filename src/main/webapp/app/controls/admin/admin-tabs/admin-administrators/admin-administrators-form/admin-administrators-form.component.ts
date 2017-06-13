import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {AppConstants} from "../../../../../app.constants";
import {AdminDialogComponent} from "../../../admin-dialog.component";

@Component({
  selector: 'admin-administrators-form',
  templateUrl: './admin-administrators-form.component.html',
  styleUrls: ['../../admin-forms.css']
})
export class AdminAdministratorsFormComponent implements OnInit {

  @Input('item') formAdministrator: any;
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
