import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MdAutocompleteModule, MdButtonModule, MdCheckboxModule, MdDatepickerModule, MdIconModule, MdInputModule,
  MdMenuModule, MdNativeDateModule, MdSelectModule, MdTooltipModule} from "@angular/material";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

import {StudentDialogComponent} from "./student-dialog.component";
import {CourseStudentDialogComponent} from "./course-student-dialog.component";
import {StudentGradeDialogComponent} from "./grade-dialog.component";

@NgModule({
  declarations: [
    StudentDialogComponent,
    StudentGradeDialogComponent,
    CourseStudentDialogComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    MdButtonModule,
    MdIconModule,
    MdTooltipModule,
    MdInputModule,
    MdMenuModule,
    MdAutocompleteModule,
    MdCheckboxModule,
    MdDatepickerModule,
    MdNativeDateModule,
    MdSelectModule,
    FormsModule,
    ReactiveFormsModule
  ],
  entryComponents: [
    StudentDialogComponent,
    StudentGradeDialogComponent,
    CourseStudentDialogComponent
  ]
})
export class OLStudentModule {}
