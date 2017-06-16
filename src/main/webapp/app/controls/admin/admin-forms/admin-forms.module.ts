import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {FlexLayoutModule} from "@angular/flex-layout";
import {ReactiveFormsModule} from "@angular/forms";
import {MdAutocompleteModule, MdButtonModule, MdCheckboxModule, MdDatepickerModule, MdIconModule, MdInputModule,
  MdMenuModule, MdNativeDateModule, MdSelectModule, MdTooltipModule} from "@angular/material";

import {AdminAchievementsFormComponent} from "./admin-achievements-form/admin-achievements-form.component";
import {AdminAdministratorsFormComponent} from "./admin-administrators-form/admin-administrators-form.component";
import {AdminCoursesFormComponent} from "./admin-courses-form/admin-courses-form.component";
import {AdminInstructorsFormComponent} from "./admin-instructors-form/admin-instructors-form.component";
import {AdminOrganizationsFormComponent} from "./admin-organizations-form/admin-organizations-form.component";
import {AdminProgramsFormComponent} from "./admin-programs-form/admin-programs-form.component";
import {AdminSessionsFormComponent} from "./admin-sessions-form/admin-sessions-form.component";
import {AdminStudentsFormComponent} from "./admin-students-form/admin-students-form.component";

@NgModule({
  declarations: [
    AdminAchievementsFormComponent,
    AdminAdministratorsFormComponent,
    AdminCoursesFormComponent,
    AdminInstructorsFormComponent,
    AdminOrganizationsFormComponent,
    AdminProgramsFormComponent,
    AdminSessionsFormComponent,
    AdminStudentsFormComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    MdAutocompleteModule,
    MdButtonModule,
    MdCheckboxModule,
    MdDatepickerModule,
    MdIconModule,
    MdInputModule,
    MdMenuModule,
    MdNativeDateModule,
    MdSelectModule,
    MdTooltipModule,
    ReactiveFormsModule
  ],
  exports: [
    AdminAchievementsFormComponent,
    AdminAdministratorsFormComponent,
    AdminCoursesFormComponent,
    AdminInstructorsFormComponent,
    AdminOrganizationsFormComponent,
    AdminProgramsFormComponent,
    AdminSessionsFormComponent,
    AdminStudentsFormComponent
  ]
})
export class OLAdminFormsModule {}
