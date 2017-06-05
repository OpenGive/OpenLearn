import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-programs-form',
  templateUrl: './admin-programs-form.component.html',
  styleUrls: ['./admin-programs-form.component.css']
})
export class AdminProgramsFormComponent implements OnInit {

  @Input('item') formProgram: any;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminProgramsFormComponent>) {}

  ngOnInit() {
    this.getRoles();
    console.log(this.formProgram);
  }

  getRoles(): void {
    this.roles = Object.keys(Role).map(key => Role[key]);
  }
}
