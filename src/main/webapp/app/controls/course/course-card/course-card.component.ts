import { Course } from './../../../models/course';
import { CourseViewComponent } from './../course-view/course-view.component'
import {Component, Input, OnInit} from '@angular/core';
import {MdDialog} from '@angular/material';

@Component({
  selector: 'app-course-card',
  templateUrl: './course-card.component.html',
  styleUrls: ['./course-card.component.css']
})
export class CourseCardComponent implements OnInit {

  @Input() course:Course;
  @Input() studentView: boolean = false;

  constructor(public dialog: MdDialog) {
  }

  ngOnInit() {
  }

  viewCourse() {
    let dialogRef = this.dialog.open(CourseViewComponent, { data: this.course });

  }
}
