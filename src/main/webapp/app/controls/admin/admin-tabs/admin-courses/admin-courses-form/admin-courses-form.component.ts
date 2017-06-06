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

  constructor(public dialogRef: MdDialogRef<AdminCoursesFormComponent>) {}

  ngOnInit() {
    console.log(this.formCourse);
  }
}
