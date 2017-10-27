import {HttpWrapperService} from './shared/auth/http-wrapper.service';
import {AppRoutingModule} from "./app-routing.module";
import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {AppComponent} from "./app.component";
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {NavigationMenuComponent} from "./controls/navigation-menu/navigation-menu.component";
import {MaterialModule, MdNativeDateModule} from "@angular/material";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FlexLayoutModule} from "@angular/flex-layout";
import "hammerjs";
import {CourseCardComponent} from "./controls/course/course-card/course-card.component";
import {UserService} from "./services/user.service";
import {StudentCourseService} from "./services/student-course.service";
import {AssignmentService} from "./services/assignment.service";
import {PortfolioService} from "./services/portfolio.service";
import {UserRouteAccessService} from "./shared/auth/user-route-access-service";
import {Principal} from "./shared/auth/principal.service";
import {AccountService} from "./shared/auth/account.service";
import {LoginService} from "./services/login.service";
import {CookieModule} from "ngx-cookie";
import {AuthServerProvider} from "./shared/auth/auth-oauth2.service";
import {StateStorageService} from "./shared/auth/state-storage.service";
import {AccessDeniedPageComponent} from './pages/access-denied-page/access-denied-page.component';
import {CourseListComponent} from './controls/course/course-list/course-list.component';
import {ForgotPasswordDialogComponent} from "./controls/forgot-password-dialog/forgot-password-dialog.component";
import {AdminPageComponent} from "./pages/admin-page/admin-page.component";
import {AdminGridService} from "./services/admin-grid.service";
import {CoursePageComponent} from "./pages/course-page/course-page.component";
import {AdminService} from "./services/admin.service";
import {StudentPageComponent} from "./pages/student-page/student-page.component";
import {CourseViewComponent} from "./controls/course/course-view/course-view.component";
import {CourseGridComponent} from "./controls/course/course-grid/course-grid.component";
import {StudentGridComponent} from "./controls/student/student-grid/student-grid.component"
import {CourseResourceGridComponent} from "./controls/course/course-resource-grid/course-resource-grid.component";
import {PortfolioGridComponent} from "./controls/student/portfolio-grid/portfolio-grid.component";
import {ProfilePageComponent} from "./pages/profile-page/profile-page.component";
import {NotifyService} from "./services/notify.service";
import {OLAdminModule} from "./controls/admin/admin.module";
import {OLCourseModule} from "./controls/course/course.module";
import {OLStudentModule} from "./controls/student/student.module";
import {DashboardPageComponent} from "./pages/dashboard-page/dashboard-page.component";
import {DataService} from "./services/data.service";
import {FileUploadComponent} from './controls/fileupload/fileupload.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    LoginPageComponent,
    ForgotPasswordDialogComponent,
    AdminPageComponent,
    NavigationMenuComponent,
    CoursePageComponent,
    StudentPageComponent,
    CourseCardComponent,
    AccessDeniedPageComponent,
    CourseListComponent,
    CourseViewComponent,
    CourseGridComponent,
    StudentGridComponent,
    CourseResourceGridComponent,
    PortfolioGridComponent,
    ProfilePageComponent,
    DashboardPageComponent,
    FileUploadComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    CookieModule.forRoot(),
    FlexLayoutModule,
    FormsModule,
    HttpModule,
    MaterialModule,
    MdNativeDateModule,
    OLAdminModule,
    OLCourseModule,
    OLStudentModule,
    ReactiveFormsModule
  ],
  providers: [
    UserService,
    StudentCourseService,
    AssignmentService,
    PortfolioService,
    UserRouteAccessService,
    Principal,
    AccountService,
    LoginService,
    AuthServerProvider,
    StateStorageService,
    HttpWrapperService,
    AdminService,
    DataService,
    AdminGridService,
    NotifyService
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    ForgotPasswordDialogComponent,
    CourseViewComponent,
    ProfilePageComponent
  ]
})
export class AppModule {
}
