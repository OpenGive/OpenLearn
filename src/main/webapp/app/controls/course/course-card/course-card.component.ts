import {Component, Input} from '@angular/core';
import {MdDialog} from '@angular/material';

import {Course} from '../../../models/course';
import {CourseViewComponent} from '../course-view/course-view.component'

@Component({
  selector: 'app-course-card',
  templateUrl: './course-card.component.html',
  styleUrls: ['./course-card.component.css']
})
export class CourseCardComponent {

  @Input() course:Course;
  @Input() studentView: boolean = false;

  constructor(public dialog: MdDialog) {}

  viewCourse() {
    let dialogRef = this.dialog.open(CourseViewComponent, { data: this.course });
  }
}
