import {Component, OnInit, Inject, Input} from '@angular/core';
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

  constructor(@Inject(MD_DIALOG_DATA) public data: any,
              private adminService: AdminService) {}

  studentView: boolean;
  course: Course;

  ngOnInit(): void {
    this.studentView = this.data.studentView;
    this.course = this.data.course;
  }
}
