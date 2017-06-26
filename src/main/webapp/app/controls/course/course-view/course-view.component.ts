import { Course } from './../../../models/course';
import { CourseService } from './../../../services/course.service';
import {Component, OnInit, Inject} from '@angular/core';
import {MD_DIALOG_DATA} from '@angular/material';

@Component({
  selector: 'app-course-view',
  templateUrl: './course-view.component.html',
  styleUrls: ['./course-view.component.css']
})
export class CourseViewComponent implements OnInit {

  constructor(@Inject(MD_DIALOG_DATA) public course: Course, private courseService: CourseService) {
  }

  ngOnInit() {
    this.courseService.get(this.course.id).subscribe(course => {this.course = course});
  }

}
