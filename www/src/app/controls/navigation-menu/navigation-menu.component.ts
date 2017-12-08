import {Component} from '@angular/core';
import {Router} from "@angular/router";

import {Principal} from "../../shared/auth/principal-storage.service";
import {AppConstants} from "../../app.constants";
import {LogoutService} from "../../services/logout.service";

@Component({
  selector: 'app-navigation-menu',
  templateUrl: './navigation-menu.component.html',
  styleUrls: ['./navigation-menu.component.css']
})
export class NavigationMenuComponent {

  name: string;
  roleDescription: string;
  roles = {
    admin: false,
    orgAdmin: false,
    instructor: false,
    student: false
  };

  private roleDescriptions = {
    Admin: 'Administrator',
    OrgAdmin: 'Org Administrator',
    Instructor: 'Instructor',
    Student: 'Student'
  };

  constructor(private router: Router,
              private logoutService: LogoutService,
              private principal: Principal) {}

  authenticated(): boolean {
    let authenticated = this.principal.isIdentityResolved();
    if (authenticated) {
      this.refreshMenu();
    }
    return authenticated;
  }

  private refreshMenu(): void {
    this.name = this.principal.getName();
    this.setRoles();
    this.setRoleDescription();
  }

  private setRoles(): void {
    let role = this.principal.getRole();
    this.roles.admin = role === AppConstants.Role.Admin.name;
    this.roles.orgAdmin = role === AppConstants.Role.OrgAdmin.name;
    this.roles.instructor = role === AppConstants.Role.Instructor.name;
    this.roles.student = role === AppConstants.Role.Student.name;
  }

  private setRoleDescription(): void {
    let role = this.principal.getRole();
    if (role === AppConstants.Role.Admin.name) {
      this.roleDescription = this.roleDescriptions.Admin;
    } else if (role === AppConstants.Role.OrgAdmin.name) {
      this.roleDescription = this.roleDescriptions.OrgAdmin;
    } else if (role === AppConstants.Role.Instructor.name) {
      this.roleDescription = this.roleDescriptions.Instructor;
    } else if (role === AppConstants.Role.Student.name) {
      this.roleDescription = this.roleDescriptions.Student;
    } else {
      this.roleDescription = 'UNAUTHORIZED';
    }
  }

  logout(): void {
    this.logoutService.logout();
  }

  toStudentPage()
  {
    this.router.navigate(['/students/' + this.principal.getId()]);
  }
}
