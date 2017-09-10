import {HttpWrapperService} from './shared/auth/http-wrapper.service';
import {AppRoutingModule} from "./app-routing.module";
import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {AppComponent} from "./app.component";
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {PortfolioPageComponent} from "./pages/portfolio-page/portfolio-page.component";
import {NavigationMenuComponent} from "./controls/navigation-menu/navigation-menu.component";
import {MaterialModule, MdNativeDateModule} from "@angular/material";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FlexLayoutModule} from "@angular/flex-layout";
import "hammerjs";
import {PortfolioCardComponent} from "./controls/portfolio/portfolio-card/portfolio-card.component";
import {CourseCardComponent} from "./controls/course/course-card/course-card.component";
import {UserService} from "./services/user.service";
import {CourseService} from "./services/course.service";
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
import {PortfolioService} from './services/portfolio.service';
import {PortfolioListComponent} from './controls/portfolio/portfolio-list/portfolio-list.component';
import {AdminGridService} from "./services/admin-grid.service";
import {CoursePageComponent} from "./pages/course-page/course-page.component";
import {AdminService} from "./services/admin.service";
import {CourseViewComponent} from "./controls/course/course-view/course-view.component";
import {CourseGridComponent} from "./controls/course/course-grid/course-grid.component";
import {CourseResourceGridComponent} from "./controls/course/course-resource-grid/course-resource-grid.component";
import {ItemLinkService} from "./services/itemlink.service";
import {ProfilePageComponent} from "./pages/profile-page/profile-page.component";
import {NotifyService} from "./services/notify.service";
import {OLAdminModule} from "./controls/admin/admin.module";
import {OLCourseModule} from "./controls/course/course.module";
import {DashboardPageComponent} from "./pages/dashboard-page/dashboard-page.component";
import {ResetPasswordPageComponent} from "./pages/reset-password-page/reset-password-page.component";
import {DataService} from "./services/course.data.service";

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    LoginPageComponent,
    ForgotPasswordDialogComponent,
    AdminPageComponent,
    NavigationMenuComponent,
    PortfolioCardComponent,
    CoursePageComponent,
    CourseCardComponent,
    AccessDeniedPageComponent,
    CourseListComponent,
    PortfolioPageComponent,
    PortfolioListComponent,
    CourseViewComponent,
    CourseGridComponent,
    CourseResourceGridComponent,
    ProfilePageComponent,
    DashboardPageComponent,
    ResetPasswordPageComponent
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
    ReactiveFormsModule
  ],
  providers: [
    UserService,
    CourseService,
    UserRouteAccessService,
    Principal,
    AccountService,
    LoginService,
    AuthServerProvider,
    StateStorageService,
    HttpWrapperService,
    PortfolioService,
    AdminService,
    DataService,
    AdminGridService,
    NotifyService,
    ItemLinkService
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
