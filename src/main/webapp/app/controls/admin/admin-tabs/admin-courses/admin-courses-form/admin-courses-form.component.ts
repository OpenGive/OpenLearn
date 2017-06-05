import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-courses-form',
  templateUrl: './admin-courses-form.component.html',
  styleUrls: ['./admin-courses-form.component.css']
})
export class AdminCoursesFormComponent implements OnInit {

  @Input('item') formCourse: any;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminCoursesFormComponent>) {}

  ngOnInit() {
    this.getRoles();
    console.log(this.formCourse);
  }

  getRoles(): void {
    this.roles = Object.keys(Role).map(key => Role[key]);
  }
}
