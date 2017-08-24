import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MdAutocompleteModule, MdButtonModule, MdCheckboxModule, MdDatepickerModule, MdIconModule, MdInputModule,
  MdMenuModule, MdNativeDateModule, MdSelectModule, MdTooltipModule} from "@angular/material";
import {FormBuilder, FormsModule, FormGroup, Validators, ReactiveFormsModule} from "@angular/forms";

import {CourseDialogComponent} from "./course-dialog.component";
import {GradeDialogComponent} from "./grade-dialog.component";
import {CourseStudentDialogComponent} from "./course-student-dialog.component";
import {ResourceDialogComponent} from "./resource-dialog.component"

import {CourseStudentFormComponent} from "./course-student-form/course-student-form.component";

@NgModule({
  declarations: [
    CourseDialogComponent,
    GradeDialogComponent,
    CourseStudentDialogComponent,
    ResourceDialogComponent,
    CourseStudentFormComponent
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
    CourseDialogComponent,
    GradeDialogComponent,
    CourseStudentDialogComponent,
    ResourceDialogComponent,
    CourseStudentFormComponent
  ]
})
export class OLCourseModule {}
