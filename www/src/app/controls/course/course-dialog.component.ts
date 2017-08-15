import {Component, Input, Inject, OnInit} from "@angular/core";
import {MdDialog, MdDialogRef, MD_DIALOG_DATA} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {UserService} from "../../services/user.service";
import {CourseService} from "../../services/course.service";
import {NotifyService} from "../../services/notify.service";

@Component({
  selector: 'course-dialog',
  template: `
    <div class="course-dialog">
      <div class="ol-dialog-header" fxLayout="row" fxLayoutAlign="space-between center">
        <div fxLayout="row" fxLayoutAlign="start center">
          <div><button md-button type="reset" (click)="close()"><i class="fa fa-times fa-lg"></i></button></div>
          <span class="ol-dialog-title">Add Student</span>
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
                <tr *ngFor="let student of students">
                  <td>{{student.firstName}}</td>
                  <td>{{student.lastName}}</td>
                  <td>{{student.email}}</td>
                  <td>
                    <button md-raised-button fxHide.xs (click)="add(student)" class="grid-add-button" mdTooltip="Add">
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
  styleUrls: ['./course-grid/course-grid.component.css', '../dialog-forms.css']
})
export class CourseDialogComponent implements OnInit {

  private students: any [];
  columns: any[];

  ngOnInit(): void {
    console.log("Start Students:" + this.students);
    this.columns = [
      {
        id: "firstName",
        name: "First Name"
      },
      {
        id: "lastName",
        name: "Last Name"
      },
      {
        id: "email",
        name: "Email"
      }
    ];

    this.getStudents();
    console.log("After Students: " + this.students);
  }

  constructor(private dialog: MdDialogRef<CourseDialogComponent>,
              @Inject(MD_DIALOG_DATA) public data: any,
              private userService: UserService,
              private courseService: CourseService,
              private notify: NotifyService) {}

  private getStudents(): void {
    this.userService.getStudents().subscribe(resp => {
      this.students = resp;
      console.log("RESP: " + resp);
    })
  }



  close(): void {
    this.dialog.close();
  }

  add(student): void {
    this.courseService.addStudentToCourse(this.data.course.id, student.id).subscribe(resp => {
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
