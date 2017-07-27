import {Component, OnInit, Inject} from '@angular/core';
import {MD_DIALOG_DATA} from '@angular/material';

import {AdminModel} from "../../controls/admin/admin.constants";
import {AdminService} from "../../services/admin.service";
import {UserService} from "../../services/user.service";
import {Course} from '../../models/course';

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.css']
})
export class DashboardPageComponent implements OnInit {

  constructor(private adminService: AdminService,
              private userService: UserService) {}
  courses: Course[] = [];

  ngOnInit(): void {
    this.getCourses();
  }

  private getCourses(): void {
    this.userService.getCoursesInstructedByUser().subscribe(courses => {
      this.courses = courses;
    })
  }

}
