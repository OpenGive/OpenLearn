import {CourseComponent} from "./controls/course/course/course.component";
import {PortfolioComponent} from "./controls/portfolio/portfolio/portfolio.component";
import {StudentPageComponent} from "./pages/student-page/student-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {CoursePageComponent} from "./pages/course-page/course-page.component";
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {UserRouteAccessService} from "./shared/auth/user-route-access-service";

const ROUTES: Routes = [
  {
    path: '',
    component: LandingPageComponent,
    data: {
      authenticate: true
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'login',
    component: LoginPageComponent,
    data: {
      authenticate: false
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'course/:id',
    component: CoursePageComponent,
    data: {
      authenticate: true
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'student/:id',
    component: StudentPageComponent,
    data: {
      authenticate: true
    },
    canActivate: [UserRouteAccessService],
    children: [
      {path: '', pathMatch: 'full', redirectTo: 'portfolio'},
      {path: 'portfolio', component: PortfolioComponent},
      {path: 'course', component: CourseComponent},
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
