import { StudentCoursesComponent } from './student-courses/student-courses.component';
import { AccessDeniedPageComponent } from './pages/access-denied-page/access-denied-page.component';
import { AppConstants } from './app.constants';
import {PortfolioPageComponent} from "./pages/portfolio-page/portfolio-page.component"
import {StudentPageComponent} from "./pages/student-page/student-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {CoursePageComponent} from "./pages/course-page/course-page.component";
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {ProfilePageComponent} from "./pages/profile-page/profile-page.component"
import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {UserRouteAccessService} from "./shared/auth/user-route-access-service";
import {AdminPageComponent} from "./pages/admin-page/admin-page.component";
import {AdminAdministratorsComponent} from "./controls/admin/admin-tabs/admin-administrators.component";
import {AdminInstructorsComponent} from "./controls/admin/admin-tabs/admin-instructors.component";
import {AdminStudentsComponent} from "./controls/admin/admin-tabs/admin-students.component";
import {AdminOrganizationsComponent} from "./controls/admin/admin-tabs/admin-organizations.component";
import {AdminSessionsComponent} from "./controls/admin/admin-tabs/admin-sessions.component";
import {AdminProgramsComponent} from "./controls/admin/admin-tabs/admin-programs.component";
import {AdminCoursesComponent} from "./controls/admin/admin-tabs/admin-courses.component";
import {AdminModel} from "./controls/admin/admin.constants";

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
    },
  },
  {
    path: 'login',
    component: LoginPageComponent,
    data: {
      authorities: []
    },
  },
  {
    path: 'admin',
    component: AdminPageComponent,
    data: {
      authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin, AppConstants.Role.Instructor]
    },
    canActivate: [UserRouteAccessService],
    children: [
      {
        path: 'organizations',
        component: AdminOrganizationsComponent,
        data: {
          authorities: AdminModel.Organization.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: 'administrators',
        component: AdminAdministratorsComponent,
        data: {
          authorities: AdminModel.Administrator.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: 'instructors',
        component: AdminInstructorsComponent,
        data: {
          authorities: AdminModel.Instructor.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: 'students',
        component: AdminStudentsComponent,
        data: {
          authorities: AdminModel.Student.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: 'sessions',
        component: AdminSessionsComponent,
        data: {
          authorities: AdminModel.Session.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: 'programs',
        component: AdminProgramsComponent,
        data: {
          authorities: AdminModel.Program.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: 'courses',
        component: AdminCoursesComponent,
        data: {
          authorities: AdminModel.Course.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'students'
      }
    ]
  },
  {
    path: 'course/:id',
    component: CoursePageComponent,
    data: {
      authorities: [AppConstants.Role.Instructor]
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'student/:id',
    component: StudentPageComponent,
    data: {
       authorities: [AppConstants.Role.Student]
    },
    canActivate: [UserRouteAccessService],
    children: [
      {path: '', pathMatch: 'full', redirectTo: 'portfolio'},
      {path: 'portfolio', component: PortfolioPageComponent},
      {path: 'course', component: StudentCoursesComponent},
    ]
  },
  {
    path: 'profile',
    component: ProfilePageComponent,
    data: {
       authorities: [AppConstants.Role.Instructor, AppConstants.Role.Student]
    },
    canActivate: [UserRouteAccessService]
  }
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
