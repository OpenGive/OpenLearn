import {Component} from '@angular/core';

import {LoginService} from "../../services/login.service";
import {Principal} from "../../shared/auth/principal.service";
import {AppConstants} from "../../app.constants";

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

  constructor(private loginService: LoginService,
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
    let roles = this.principal.getRoles();
    this.roles.admin = roles.includes(AppConstants.Role.Admin);
    this.roles.orgAdmin = roles.includes(AppConstants.Role.OrgAdmin);
    this.roles.instructor = roles.includes(AppConstants.Role.Instructor);
    this.roles.student = roles.includes(AppConstants.Role.Student);
  }

  private setRoleDescription(): void {
    let roles = this.principal.getRoles();
    if (roles.includes(AppConstants.Role.Admin)) {
      this.roleDescription = this.roleDescriptions.Admin;
    } else if (roles.includes(AppConstants.Role.OrgAdmin)) {
      this.roleDescription = this.roleDescriptions.OrgAdmin;
    } else if (roles.includes(AppConstants.Role.Instructor)) {
      this.roleDescription = this.roleDescriptions.Instructor;
    } else if (roles.includes(AppConstants.Role.Student)) {
      this.roleDescription = this.roleDescriptions.Student;
    } else {
      this.roleDescription = 'UNAUTHORIZED';
    }
  }

  logout(): void {
    this.loginService.logout();
  }
}
