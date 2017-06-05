import { Course } from './../../models/course';
import { CourseService } from './../../services/course.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-course-page',
  templateUrl: './course-page.component.html',
  styleUrls: ['./course-page.component.css']
})
export class CoursePageComponent implements OnInit {

  constructor(private _courseService: CourseService) { }
  courses: Course[] = [];
  ngOnInit() {
    this._courseService.getAll().subscribe(courses => {
      console.log(courses);
      this.courses = courses;
    });
  }

}
