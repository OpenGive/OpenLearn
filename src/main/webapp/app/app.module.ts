import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AuthenticationService } from './shared/authentication/authentication.service';
import { AppRoutingModule } from './app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { MaterialModule } from "@angular/material";
import { NavigationComponent } from './shared/navigation/navigation.component';
import { LandingPageComponent } from "./pages/landing-page/landing-page.component";
import { AdminPageComponent } from './pages/admin-page/admin-page.component';
import { ProgramPageComponent } from './pages/program-page/program-page.component';
import { StudentPageComponent } from './pages/student-page/student-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { IsLoggedInGuardService } from "./shared/authentication/is-logged-in-guard.service";

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    LandingPageComponent, LoginPageComponent, ProgramPageComponent, StudentPageComponent, AdminPageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    MaterialModule.forRoot(),
    BrowserAnimationsModule,
    AppRoutingModule,
    
  ],
  providers: [AuthenticationService, IsLoggedInGuardService],
  bootstrap: [AppComponent]
})
export class AppModule { }
