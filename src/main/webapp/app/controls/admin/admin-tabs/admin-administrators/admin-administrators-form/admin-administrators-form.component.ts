import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {AppConstants} from "../../../../../app.constants";

@Component({
  selector: 'admin-administrators-form',
  templateUrl: './admin-administrators-form.component.html',
  styleUrls: ['./admin-administrators-form.component.css', '../../admin-forms.css']
})
export class AdminAdministratorsFormComponent implements OnInit {

  @Input('item') formAdministrator: any;
  @Input() editing: boolean;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminAdministratorsFormComponent>) {}

  ngOnInit() {
    this.getRoles();
  }

  getRoles(): void {
    this.roles = Object.keys(AppConstants.Role).map(key => AppConstants.Role[key]);
  }
}
