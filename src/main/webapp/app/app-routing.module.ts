import { StudentCoursesComponent } from './student-courses/student-courses.component';
import { AccessDeniedPageComponent } from './pages/access-denied-page/access-denied-page.component';
import { Role } from './app.constants';
import {PortfolioComponent} from "./controls/portfolio/portfolio/portfolio.component";
import {StudentPageComponent} from "./pages/student-page/student-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {CoursePageComponent} from "./pages/course-page/course-page.component";
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";

const ROUTES: Routes = [
  {
    path: '',
    component: LandingPageComponent,
    data: {
      authorities: []
    },
  },
  {
    path: 'access-denied',
    component: AccessDeniedPageComponent,
    data: {
      authorities: []
    }
  },
  {
    path: 'login',
    component: LoginPageComponent,
    data: {
      authorities: []
    }
  },
  {
    path: 'course/:id',
    component: CoursePageComponent,
    data: {
      authorities: [Role.Instructor]
    }
  },
  {
    path: 'student/:id',
    component: StudentPageComponent,
    data: {
       authorities: [Role.Student]
    },
    children: [
      {path: '', pathMatch: 'full', redirectTo: 'portfolio'},
      {path: 'portfolio', component: PortfolioComponent},
      {path: 'course', component: StudentCoursesComponent},
    ]
  },

];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(ROUTES)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
