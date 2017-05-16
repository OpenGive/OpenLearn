import { Course } from './../models/course';
import { CourseService } from './../services/course.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-student-courses',
  templateUrl: './student-courses.component.html',
  styleUrls: ['./student-courses.component.css']
})
export class StudentCoursesComponent implements OnInit {

  constructor(private courseService: CourseService) { }
studentCourses: Course[] = [];
  ngOnInit() {
     this.courseService.getAllCourses().subscribe(courses => {
      this.studentCourses = courses
    });
  }

}
