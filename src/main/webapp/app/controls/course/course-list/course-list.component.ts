import { Course } from './../../../models/course';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-course-list',
  templateUrl: './course-list.component.html',
  styleUrls: ['./course-list.component.css']
})
export class CourseListComponent implements OnInit {

  constructor() { }
  @Input() courses: Course[] = [];
  @Input() studentView: boolean = false;
  ngOnInit() {
  }

}
