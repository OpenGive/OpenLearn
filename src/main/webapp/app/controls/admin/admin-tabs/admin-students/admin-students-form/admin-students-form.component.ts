import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-students-form',
  templateUrl: './admin-students-form.component.html',
  styleUrls: ['./admin-students-form.component.css']
})
export class AdminStudentsFormComponent implements OnInit {

  @Input('item') formStudent: any;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminStudentsFormComponent>) {}

  ngOnInit() {
    this.getRoles();
    console.log(this.formStudent);
  }

  getRoles(): void {
    this.roles = Object.keys(Role).map(key => Role[key]);
  }
}
