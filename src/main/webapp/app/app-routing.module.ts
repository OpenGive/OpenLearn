import { AdminPageComponent } from './pages/admin-page/admin-page.component';
import { ProgramPageComponent } from './pages/program-page/program-page.component';
import { StudentPageComponent } from './pages/student-page/student-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from "@angular/router";
import { LandingPageComponent } from "./pages/landing-page/landing-page.component";

export const ROUTES: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'program/:id', component: ProgramPageComponent },
  { path: 'student/:id', component: StudentPageComponent },
  { path: 'admin', component: AdminPageComponent },
]

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(ROUTES)
  ],
  declarations: [LandingPageComponent, LoginPageComponent, ProgramPageComponent, StudentPageComponent, AdminPageComponent],
  exports: [RouterModule, LandingPageComponent, LoginPageComponent, ProgramPageComponent, StudentPageComponent, AdminPageComponent],
  
})
export class AppRoutingModule { }
