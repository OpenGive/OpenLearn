import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MdButtonModule, MdIconModule, MdTooltipModule} from "@angular/material";

import {AdminAchievementsComponent} from "./admin-tabs/admin-achievements.component";
import {AdminAdministratorsComponent} from "./admin-tabs/admin-administrators.component";
import {AdminCoursesComponent} from "./admin-tabs/admin-courses.component";
import {AdminDialogComponent} from "./admin-dialog.component";
import {AdminGridComponent} from "./admin-grid/admin-grid.component";
import {AdminInstructorsComponent} from "./admin-tabs/admin-instructors.component";
import {AdminMilestonesComponent} from "./admin-tabs/admin-milestones.component";
import {AdminOrganizationsComponent} from "./admin-tabs/admin-organizations.component";
import {AdminProgramsComponent} from "./admin-tabs/admin-programs.component";
import {AdminSessionsComponent} from "./admin-tabs/admin-sessions.component";
import {AdminStudentsComponent} from "./admin-tabs/admin-students.component";
import {OLAdminFormsModule} from "./admin-forms/admin-forms.module";

@NgModule({
  declarations: [
    AdminAchievementsComponent,
    AdminAdministratorsComponent,
    AdminCoursesComponent,
    AdminDialogComponent,
    AdminGridComponent,
    AdminInstructorsComponent,
    AdminMilestonesComponent,
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
  entryComponents: [
    AdminDialogComponent
  ]
})
export class OLAdminModule {}
