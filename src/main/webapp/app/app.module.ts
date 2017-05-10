import { AppRoutingModule } from './app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { StudentPageComponent } from './pages/student-page/student-page.component';
import { CoursePageComponent } from './pages/course-page/course-page.component';
import { NavigationMenuComponent } from './controls/navigation-menu/navigation-menu.component';
import { MaterialModule } from "@angular/material";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FlexLayoutModule } from "@angular/flex-layout";
import 'hammerjs';
import { PortfolioComponent } from './controls/portfolio/portfolio/portfolio.component';
import { PortfolioCardComponent } from './controls/portfolio/portfolio-card/portfolio-card.component';
import { CourseCardComponent } from './controls/course/course-card/course-card.component';
import { CourseComponent } from './controls/course/course/course.component';
import { ParallaxHeaderComponent } from './controls/layout/parallax-header/parallax-header.component';

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
    ParallaxHeaderComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    MaterialModule.forRoot(),
    BrowserAnimationsModule,
    FlexLayoutModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
