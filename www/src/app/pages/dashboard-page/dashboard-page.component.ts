import {Component, OnInit} from '@angular/core';

import {UserService} from "../../services/user.service";
import {Course} from '../../models/course.model';

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.css']
})
export class DashboardPageComponent implements OnInit {

  constructor(private userService: UserService) {}
  courses: Course[];

  ngOnInit(): void {
    this.getCourses();
  }

  private getCourses(): void {
    this.userService.getCoursesInstructedByUser().subscribe(courses => {
      this.courses = courses;
    })
  }

}
