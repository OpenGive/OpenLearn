import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MdAutocompleteModule, MdButtonModule, MdCheckboxModule, MdDatepickerModule, MdIconModule, MdInputModule,
  MdMenuModule, MdNativeDateModule, MdSelectModule, MdTooltipModule} from "@angular/material";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

import {CourseDialogComponent} from "./course-dialog.component";
import {AssignmentFormComponent} from "./assignment/assignment-form.component";
import {PortfolioFormComponent} from "./portfolio/portfolio-form.component";
import {GradeDialogComponent} from "./grade-dialog.component";
import {FileUploadComponent} from '../fileupload/fileupload.component';
import {FilesGridComponent} from './files-grid/files-grid.component';
import { FileUploadModule } from "ng2-file-upload";

@NgModule({
  declarations: [
    CourseDialogComponent,
    AssignmentFormComponent,
    PortfolioFormComponent,
    GradeDialogComponent,
    FilesGridComponent,
    FileUploadComponent
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
    ReactiveFormsModule,
    FileUploadModule
  ],
  entryComponents: [
    CourseDialogComponent,
    AssignmentFormComponent,
    PortfolioFormComponent,
    GradeDialogComponent,
    FileUploadComponent,
    FilesGridComponent
  ]
})
export class OLCourseModule {}
