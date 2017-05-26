import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-users-form',
  templateUrl: './admin-users-form.component.html',
  styleUrls: ['./admin-users-form.component.css']
})
export class AdminUsersFormComponent implements OnInit {

  @Input('item') formAdminUser: any;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminUsersFormComponent>) {}

  ngOnInit() {
    this.getRoles();
    console.log(this.formAdminUser);
  }

  getRoles(): void {
    this.roles = Object.keys(Role).map(key => Role[key]);
  }
}
