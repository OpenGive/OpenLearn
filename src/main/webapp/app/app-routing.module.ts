import { FormsModule } from '@angular/forms';
import { AuthenticationService } from './shared/authentication/authentication.service';
import { IsLoggedInGuardService } from './shared/authentication/is-logged-in-guard.service';
import { MaterialModule } from '@angular/material';
import { LandingPageComponent } from "./pages/landing-page/landing-page.component";
import { AdminPageComponent } from './pages/admin-page/admin-page.component';
import { ProgramPageComponent } from './pages/program-page/program-page.component';
import { StudentPageComponent } from './pages/student-page/student-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from "@angular/router";

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import 'hammerjs';

export const ROUTES: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'program/:id', component: ProgramPageComponent },
  { path: 'student/:id', component: StudentPageComponent, canActivate: [IsLoggedInGuardService] 
},
  { path: 'admin', component: AdminPageComponent },
]

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(ROUTES),
  ],
  exports: [RouterModule],
  declarations: [],
})
export class AppRoutingModule { }
