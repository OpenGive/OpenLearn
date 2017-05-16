import { Course } from './../../../models/course';
import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-course-card',
  templateUrl: './course-card.component.html',
  styleUrls: ['./course-card.component.css']
})
export class CourseCardComponent implements OnInit {

  @Input() course:Course;

  constructor() {
  }

  ngOnInit() {
  }

}
