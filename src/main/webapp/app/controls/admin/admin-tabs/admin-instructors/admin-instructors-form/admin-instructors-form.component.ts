import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-instructors-form',
  templateUrl: './admin-instructors-form.component.html',
  styleUrls: ['./admin-instructors-form.component.css']
})
export class AdminInstructorsFormComponent implements OnInit {

  @Input('item') formInstructor: any;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminInstructorsFormComponent>) {}

  ngOnInit() {
    this.getRoles();
    console.log(this.formInstructor);
  }

  getRoles(): void {
    this.roles = Object.keys(Role).map(key => Role[key]);
  }
}
