import { CourseService } from './services/course.service';
import { HttpWrapperService } from './shared/auth/http-wrapper.service';
import {AppRoutingModule} from "./app-routing.module";
import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {AppComponent} from "./app.component";
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {StudentPageComponent} from "./pages/student-page/student-page.component";
import {CoursePageComponent} from "./pages/course-page/course-page.component";
import {NavigationMenuComponent} from "./controls/navigation-menu/navigation-menu.component";
import {MaterialModule} from "@angular/material";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FlexLayoutModule} from "@angular/flex-layout";
import "hammerjs";
import {PortfolioComponent} from "./controls/portfolio/portfolio/portfolio.component";
import {PortfolioCardComponent} from "./controls/portfolio/portfolio-card/portfolio-card.component";
import {CourseCardComponent} from "./controls/course/course-card/course-card.component";
import {CourseComponent} from "./controls/course/course/course.component";
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

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    LoginPageComponent,
    StudentPageComponent,
    CoursePageComponent,
    NavigationMenuComponent,
    PortfolioComponent,
    PortfolioCardComponent,
    CourseCardComponent,
    CourseComponent,
    ParallaxHeaderComponent,
    AccessDeniedPageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    MaterialModule,
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
    CourseService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
