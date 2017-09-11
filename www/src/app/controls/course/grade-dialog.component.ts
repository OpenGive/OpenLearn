import {Component, Inject, OnInit} from "@angular/core";
import {MdDialogRef, MD_DIALOG_DATA} from "@angular/material";
import {StudentCourseService} from "../../services/student-course.service";
import {NotifyService} from "../../services/notify.service";

@Component({
  selector: 'grade-dialog',
  template: `
    <div class="course-dialog">
      <div class="ol-dialog-header" fxLayout="row" fxLayoutAlign="space-between center">
        <div fxLayout="row" fxLayoutAlign="start center">
          <div><button md-button type="reset" (click)="close()"><i class="fa fa-times fa-lg"></i></button></div>
          <span class="ol-dialog-title">Edit Grade</span>
        </div>
      </div>
      <div class="ol-dialog-content course-view-body">
        <div>
          <md-input-container fxFlex="100%">
            <input [(ngModel)]="grade" mdInput type="number" required md-maxlength="3">
          </md-input-container>
          <button (click)="save()" md-raised-button type="button" class="ol-dialog-button grey-button" mdTooltip="Save">
            <md-icon>save</md-icon>
          </button>
        </div>
      </div>
    </div>
    `,
  styleUrls: ['./course-grid/course-grid.component.css', '../dialog-forms.css']
})
export class GradeDialogComponent implements OnInit {

  grade: Number;

  ngOnInit(): void {
    this.grade = this.data.student.grade;
  }

  constructor(private dialog: MdDialogRef<GradeDialogComponent>,
              @Inject(MD_DIALOG_DATA) public data: any,
              private courseService: StudentCourseService,
              private notify: NotifyService) {}

  close(): void {
    this.dialog.close();
  }

  save(): void {
    this.data.student.grade = this.grade;
    this.courseService.updateStudentCourse(this.data.student.id).subscribe(resp => {
      this.dialog.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully set student grade');
    }, error => {
      this.notify.error('Failed to set student grade');
    });
  }
}
