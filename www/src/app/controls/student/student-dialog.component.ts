import {Component, Input, Inject, OnInit} from "@angular/core";
import {MdDialog, MdDialogRef, MD_DIALOG_DATA} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {StudentCourseService} from "../../services/student-course.service";
import {NotifyService} from "../../services/notify.service";
import {AdminService} from "../../services/admin.service";
import {AdminTabs} from "../admin/admin.constants";

@Component({
  selector: 'student-dialog',
  template: `
    <div class="course-dialog">
      <div class="ol-dialog-header" fxLayout="row" fxLayoutAlign="space-between center">
        <div fxLayout="row" fxLayoutAlign="start center">
          <div><button md-button type="reset" (click)="close()"><i class="fa fa-times fa-lg"></i></button></div>
          <span class="ol-dialog-title">Add Course</span>
        </div>
      </div>
      <div class="ol-dialog-content course-view-body">
        <div>
          <div id="grid-table" fxLayout="row">
            <div class="table-wrapper">
              <table>
                <thead>
                <tr class="header-row">
                  <th *ngFor="let column of columns" (click)="sort(column)">
                    {{column.name}}
                    <md-icon>{{column.sortIcon}}</md-icon>
                  </th>
                  <th>

                  </th>
                </tr>
                </thead>
                <tbody>
                <tr *ngFor="let course of courses">
                  <td>{{course.name}}</td>
                  <td>{{course.description}}</td>
                  <td>
                    <button md-raised-button fxHide.xs (click)="add(course)" class="grid-add-button" mdTooltip="Add">
                      <md-icon>add</md-icon>
                    </button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    `,
  styleUrls: ['./student-grid/student-grid.component.css', '../dialog-forms.css']
})
export class StudentDialogComponent implements OnInit {

  courses: any [];
  columns: any[];

  ngOnInit(): void {
    console.log(this.data);
    this.columns = [
      {
        id: "name",
        name: "Name"
      },
      {
        id: "description",
        name: "Description"
      }
    ];

    this.getCourses();
  }

  constructor(private dialog: MdDialogRef<StudentDialogComponent>,
              @Inject(MD_DIALOG_DATA) public data: any,
              private courseService: StudentCourseService,
              private adminService: AdminService,
              private notify: NotifyService) {}

  private getCourses(): void {
    this.adminService.getAll(AdminTabs.Course.route).subscribe(resp => {
      this.courses = resp;
    });
  }



  close(): void {
    this.dialog.close();
  }

  add(course): void {
    this.courseService.createStudentCourse(this.data.student.id, course.id).subscribe(resp => {
      this.dialog.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added student to course');
    }, error => {
      this.notify.error('Failed to add student to course');
    });
  }
}
