import { CourseService } from './services/course.service';
import { HttpWrapperService } from './shared/auth/http-wrapper.service';
import {AppRoutingModule} from "./app-routing.module";
import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {AppComponent} from "./app.component";
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {StudentPageComponent} from "./pages/student-page/student-page.component";
import {CoursePageComponent} from "./pages/course-page/course-page.component";
import {PortfolioPageComponent} from "./pages/portfolio-page/portfolio-page.component";
import {NavigationMenuComponent} from "./controls/navigation-menu/navigation-menu.component";
import {MaterialModule, MdNativeDateModule} from "@angular/material";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FlexLayoutModule} from "@angular/flex-layout";
import "hammerjs";
import {PortfolioCardComponent} from "./controls/portfolio/portfolio-card/portfolio-card.component";
import {CourseCardComponent} from "./controls/course/course-card/course-card.component";
import {ParallaxHeaderComponent} from "./controls/layout/parallax-header/parallax-header.component";
import {UserService} from "./services/user.service";
import {UserRouteAccessService} from "./shared/auth/user-route-access-service";
import {Principal} from "./shared/auth/principal.service";
import {AccountService} from "./shared/auth/account.service";
import {LoginService} from "./shared/login/login.service";
import {CookieModule} from "ngx-cookie";
import {AuthServerProvider} from "./shared/auth/auth-oauth2.service";
import {StateStorageService} from "./shared/auth/state-storage.service";
import { AccessDeniedPageComponent } from './pages/access-denied-page/access-denied-page.component';
import { CourseListComponent } from './controls/course/course-list/course-list.component';
import { StudentCoursesComponent } from './student-courses/student-courses.component';
import {ForgotPasswordDialogComponent} from "./controls/forgot-password-dialog/forgot-password-dialog.component";
import {AdminPageComponent} from "./pages/admin-page/admin-page.component";
import { PortfolioService } from './services/portfolio.service';
import { PortfolioListComponent } from './controls/portfolio/portfolio-list/portfolio-list.component'
import {AdminGridComponent} from "./controls/admin/admin-grid/admin-grid.component";
import {AdminDialogComponent} from "./controls/admin/admin-dialog.component";
import {AdminGridService} from "./services/admin-grid.service";
import {AdminOrganizationsComponent} from "./controls/admin/admin-tabs/admin-organizations/admin-organizations.component";
import {AdminAdministratorsComponent} from "./controls/admin/admin-tabs/admin-administrators/admin-administrators.component";
import {AdminInstructorsComponent} from "./controls/admin/admin-tabs/admin-instructors/admin-instructors.component";
import {AdminStudentsComponent} from "./controls/admin/admin-tabs/admin-students/admin-students.component";
import {AdminSessionsComponent} from "./controls/admin/admin-tabs/admin-sessions/admin-sessions.component";
import {AdminProgramsComponent} from "./controls/admin/admin-tabs/admin-programs/admin-programs.component";
import {AdminCoursesComponent} from "./controls/admin/admin-tabs/admin-courses/admin-courses.component";
import {AdminOrganizationsFormComponent} from "./controls/admin/admin-tabs/admin-organizations/admin-organizations-form/admin-organizations-form.component";
import {AdminAdministratorsFormComponent} from "./controls/admin/admin-tabs/admin-administrators/admin-administrators-form/admin-administrators-form.component";
import {AdminInstructorsFormComponent} from "./controls/admin/admin-tabs/admin-instructors/admin-instructors-form/admin-instructors-form.component";
import {AdminStudentsFormComponent} from "./controls/admin/admin-tabs/admin-students/admin-students-form/admin-students-form.component";
import {AdminSessionsFormComponent} from "./controls/admin/admin-tabs/admin-sessions/admin-sessions-form/admin-sessions-form.component";
import {AdminProgramsFormComponent} from "./controls/admin/admin-tabs/admin-programs/admin-programs-form/admin-programs-form.component";
import {AdminCoursesFormComponent} from "./controls/admin/admin-tabs/admin-courses/admin-courses-form/admin-courses-form.component";
import {AdminService} from "./services/admin.service";
import {CourseViewComponent} from "./controls/course/course-view/course-view.component"
import {CourseActivityListComponent } from './controls/course/course-activity-list/course-activity-list.component';
import {ProfilePageComponent} from "./pages/profile-page/profile-page.component";
import {NotifyService} from "./services/notify.service";

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    LoginPageComponent,
    ForgotPasswordDialogComponent,
    AdminPageComponent,
    AdminDialogComponent,
    AdminGridComponent,
    AdminOrganizationsComponent,
    AdminAdministratorsComponent,
    AdminInstructorsComponent,
    AdminStudentsComponent,
    AdminSessionsComponent,
    AdminProgramsComponent,
    AdminCoursesComponent,
    AdminOrganizationsFormComponent,
    AdminAdministratorsFormComponent,
    AdminInstructorsFormComponent,
    AdminStudentsFormComponent,
    AdminSessionsFormComponent,
    AdminProgramsFormComponent,
    AdminCoursesFormComponent,
    StudentPageComponent,
    CoursePageComponent,
    NavigationMenuComponent,
    PortfolioCardComponent,
    CourseCardComponent,
    ParallaxHeaderComponent,
    AccessDeniedPageComponent,
    CourseListComponent,
    StudentCoursesComponent,
    PortfolioPageComponent,
    PortfolioListComponent,
    CourseViewComponent,
    CourseActivityListComponent,
    ProfilePageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    AppRoutingModule,
    MaterialModule,
    MdNativeDateModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    CookieModule.forRoot()
  ],
  providers: [
    UserService,
    UserRouteAccessService,
    Principal,
    AccountService,
    LoginService,
    AuthServerProvider,
    StateStorageService,
    HttpWrapperService,
    CourseService,
    PortfolioService,
    AdminService,
    AdminGridService,
    NotifyService
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    AdminDialogComponent,
    ForgotPasswordDialogComponent,
    CourseViewComponent,
    ProfilePageComponent
  ]
})
export class AppModule {
}
