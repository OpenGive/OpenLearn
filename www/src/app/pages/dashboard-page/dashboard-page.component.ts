import {Component, OnInit} from '@angular/core';

import {Course} from '../../models/course.model';

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.css']
})
export class DashboardPageComponent implements OnInit {

  constructor() {}
  courses: Course[];

  ngOnInit(): void {
    this.getCourses();
  }

  private getCourses(): void {
    // TODO: Get courses by instructor (preferrably only the instructors courses)
  }

}
