import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MdButtonModule, MdIconModule, MdTooltipModule} from "@angular/material";

import {AdminAdministratorsComponent} from "./admin-tabs/admin-administrators.component";
import {AdminCoursesComponent} from "./admin-tabs/admin-courses.component";
import {AdminDialogComponent} from "./admin-dialog.component";
import {AdminGridComponent} from "./admin-grid/admin-grid.component";
import {AdminInstructorsComponent} from "./admin-tabs/admin-instructors.component";
import {AdminOrgAdministratorsComponent} from "./admin-tabs/admin-org-administrators.component";
import {AdminOrganizationsComponent} from "./admin-tabs/admin-organizations.component";
import {AdminProgramsComponent} from "./admin-tabs/admin-programs.component";
import {AdminSessionsComponent} from "./admin-tabs/admin-sessions.component";
import {AdminStudentsComponent} from "./admin-tabs/admin-students.component";
import {OLAdminFormsModule} from "./admin-forms/admin-forms.module";
import {AdminAdministratorsFormComponent} from "./admin-forms/admin-administrators-form/admin-administrators-form.component";
import {AdminOrgAdministratorsFormComponent} from "./admin-forms/admin-org-administrators-form/admin-org-administrators-form.component";
import {AdminInstructorsFormComponent} from "./admin-forms/admin-instructors-form/admin-instructors-form.component";

@NgModule({
  declarations: [
    AdminAdministratorsComponent,
    AdminOrgAdministratorsComponent,
    AdminCoursesComponent,
    AdminDialogComponent,
    AdminGridComponent,
    AdminInstructorsComponent,
    AdminOrganizationsComponent,
    AdminProgramsComponent,
    AdminSessionsComponent,
    AdminStudentsComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    MdButtonModule,
    MdIconModule,
    MdTooltipModule,
    OLAdminFormsModule
  ],
  exports: [
    AdminAdministratorsFormComponent,
    AdminOrgAdministratorsFormComponent,
    AdminInstructorsFormComponent,
  ],
  entryComponents: [
    AdminDialogComponent
  ]
})
export class OLAdminModule {}
