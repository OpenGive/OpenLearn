import {Component, OnInit} from '@angular/core';

import {AdminModel} from "../controls/admin/admin.constants";
import {AdminService} from "../services/admin.service";
import {Course} from '../models/course';

@Component({
  selector: 'app-student-courses',
  templateUrl: './student-courses.component.html',
  styleUrls: ['./student-courses.component.css']
})
export class StudentCoursesComponent implements OnInit {

  studentCourses: Course[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.getCourses();
  }

  private getCourses(): void {
    this.adminService.getAll(AdminModel.Course.route).subscribe(courses => {
      this.studentCourses = courses
    });
  }

}
