import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-administrators-form',
  templateUrl: './admin-administrators-form.component.html',
  styleUrls: ['./admin-administrators-form.component.css', '../../admin-forms.css']
})
export class AdminAdministratorsFormComponent implements OnInit {

  @Input('item') formAdministrator: any;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminAdministratorsFormComponent>) {}

  ngOnInit() {
    this.getRoles();
    console.log(this.formAdministrator);
  }

  getRoles(): void {
    this.roles = Object.keys(Role).map(key => Role[key]);
  }
}
