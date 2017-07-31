import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MdButtonModule, MdIconModule, MdTooltipModule, MdInputModule} from "@angular/material";
import {FormBuilder, FormsModule, FormGroup, Validators} from "@angular/forms";

import {CourseDialogComponent} from "./course-dialog.component";
import {GradeDialogComponent} from "./grade-dialog.component";
import {ResourceDialogComponent} from "./resource-dialog.component"

@NgModule({
  declarations: [
    CourseDialogComponent,
    GradeDialogComponent,
    ResourceDialogComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    MdButtonModule,
    MdIconModule,
    MdTooltipModule,
    MdInputModule,
    FormsModule
  ],
  entryComponents: [
    CourseDialogComponent,
    GradeDialogComponent,
    ResourceDialogComponent
  ]
})
export class OLCourseModule {}
