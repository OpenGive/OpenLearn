import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";

@Component({
  selector: 'admin-courses-form',
  templateUrl: './admin-courses-form.component.html',
  styleUrls: ['./admin-courses-form.component.css', '../../admin-forms.css']
})
export class AdminCoursesFormComponent implements OnInit {

  @Input('item') formCourse: any;
  @Input() editing: boolean;

  constructor(public dialogRef: MdDialogRef<AdminCoursesFormComponent>) {}

  ngOnInit() {
  }
}
