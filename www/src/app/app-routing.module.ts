import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";

import {AppConstants} from './app.constants';
import {AdminModel} from "./controls/admin/admin.constants";
import {AccessDeniedPageComponent} from './pages/access-denied-page/access-denied-page.component';
import {AdminPageComponent} from "./pages/admin-page/admin-page.component";
import {DashboardPageComponent} from "./pages/dashboard-page/dashboard-page.component";
import {CoursePageComponent} from "./pages/course-page/course-page.component";
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {PortfolioPageComponent} from "./pages/portfolio-page/portfolio-page.component"
import {ProfilePageComponent} from "./pages/profile-page/profile-page.component";
// import {AdminAchievementsComponent} from "./controls/admin/admin-tabs/admin-achievements.component";
import {AdminAdministratorsComponent} from "./controls/admin/admin-tabs/admin-administrators.component";
import {AdminCoursesComponent} from "./controls/admin/admin-tabs/admin-courses.component";
import {AdminInstructorsComponent} from "./controls/admin/admin-tabs/admin-instructors.component";
// import {AdminMilestonesComponent} from "./controls/admin/admin-tabs/admin-milestones.component";
import {AdminOrganizationsComponent} from "./controls/admin/admin-tabs/admin-organizations.component";
import {AdminProgramsComponent} from "./controls/admin/admin-tabs/admin-programs.component";
import {AdminSessionsComponent} from "./controls/admin/admin-tabs/admin-sessions.component";
import {AdminStudentsComponent} from "./controls/admin/admin-tabs/admin-students.component";
import {UserRouteAccessService} from "./shared/auth/user-route-access-service";

const ROUTES: Routes = [
  {
    path: '',
    component: LandingPageComponent,
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
      authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
    },
    canActivate: [UserRouteAccessService],
    children: [
      {
        path: AdminModel.Administrator.route,
        component: AdminAdministratorsComponent,
        data: {
          authorities: AdminModel.Administrator.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: AdminModel.Instructor.route,
        component: AdminInstructorsComponent,
        data: {
          authorities: AdminModel.Instructor.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: AdminModel.Student.route,
        component: AdminStudentsComponent,
        data: {
          authorities: AdminModel.Student.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: AdminModel.Organization.route,
        component: AdminOrganizationsComponent,
        data: {
          authorities: AdminModel.Organization.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: AdminModel.Session.route,
        component: AdminSessionsComponent,
        data: {
          authorities: AdminModel.Session.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: AdminModel.Program.route,
        component: AdminProgramsComponent,
        data: {
          authorities: AdminModel.Program.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: AdminModel.Course.route,
        component: AdminCoursesComponent,
        data: {
          authorities: AdminModel.Course.authorities
        },
        canActivate: [UserRouteAccessService]
      },
      // {
      //   path: AdminModel.Milestone.route,
      //   component: AdminMilestonesComponent,
      //   data: {
      //     authorities: AdminModel.Milestone.authorities
      //   },
      //   canActivate: [UserRouteAccessService]
      // },
      // {
      //   path: AdminModel.Achievement.route,
      //   component: AdminAchievementsComponent,
      //   data: {
      //     authorities: AdminModel.Achievement.authorities
      //   },
      //   canActivate: [UserRouteAccessService]
      // },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: AdminModel.Administrator.route
      },
      {
        path: '**',
        redirectTo: AdminModel.Administrator.route
      }
    ]
  },
  {
    path: 'dashboard',
    component: DashboardPageComponent,
    data: {
      authorities: [AppConstants.Role.Instructor]
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'course',
    component: CoursePageComponent,
    data: {
      authorities: []
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'portfolio',
    component: PortfolioPageComponent,
    data: {
       authorities: [AppConstants.Role.Student]
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'profile',
    component: ProfilePageComponent,
    data: {
       authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin, AppConstants.Role.Instructor, AppConstants.Role.Student]
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'access-denied',
    component: AccessDeniedPageComponent,
    data: {
      authorities: []
    },
  },
  {
    path: '**',
    redirectTo: ''
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
