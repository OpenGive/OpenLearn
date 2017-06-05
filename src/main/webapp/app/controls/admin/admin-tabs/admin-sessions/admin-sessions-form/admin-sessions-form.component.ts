import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-sessions-form',
  templateUrl: './admin-sessions-form.component.html',
  styleUrls: ['./admin-sessions-form.component.css']
})
export class AdminSessionsFormComponent implements OnInit {

  @Input('item') formSession: any;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminSessionsFormComponent>) {}

  ngOnInit() {
    this.getRoles();
    console.log(this.formSession);
  }

  getRoles(): void {
    this.roles = Object.keys(Role).map(key => Role[key]);
  }
}
