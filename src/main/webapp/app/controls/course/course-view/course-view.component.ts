import {Component, OnInit, Inject} from '@angular/core';
import {MD_DIALOG_DATA} from '@angular/material';

import {AdminModel} from "../../admin/admin.constants";
import {AdminService} from "../../../services/admin.service";
import {Course} from '../../../models/course';

@Component({
  selector: 'app-course-view',
  templateUrl: './course-view.component.html',
  styleUrls: ['./course-view.component.css', '../../dialog-forms.css']
})
export class CourseViewComponent implements OnInit {

  constructor(@Inject(MD_DIALOG_DATA) public course: Course,
              private adminService: AdminService) {}

  ngOnInit(): void {
    this.getCourses();
  }

  private getCourses(): void {
    this.adminService.get(AdminModel.Course.route, this.course.id).subscribe(course => {
      this.course = course;
    });
  }
}
