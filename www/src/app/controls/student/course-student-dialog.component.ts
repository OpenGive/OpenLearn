import {Component, Inject} from "@angular/core";
import {MD_DIALOG_DATA} from "@angular/material";

@Component({
  selector: 'course-student-dialog',
  template: `
    <div class="course-dialog">
      <!--<course-student-form [item]="data.item" [adding]="data.adding"></course-student-form>-->
    </div>
    `
})
export class CourseStudentDialogComponent {

  constructor(@Inject(MD_DIALOG_DATA) public data: any) {}
}
