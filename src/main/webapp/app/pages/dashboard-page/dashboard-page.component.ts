import {Component, OnInit, Inject} from '@angular/core';
import {MD_DIALOG_DATA} from '@angular/material';

import {AdminModel} from "../../controls/admin/admin.constants";
import {AdminService} from "../../services/admin.service";
import {Course} from '../../models/course';

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.css']
})
export class DashboardPageComponent implements OnInit {

  constructor(private adminService: AdminService) {}
  courses: Course[] = [];

  ngOnInit(): void {
    this.getCourses();
  }

  private getCourses(): void {
    this.adminService.search(AdminModel.Course.route, 'instructor:1').subscribe(courses => {
      this.courses = courses;
    });
  }

}
