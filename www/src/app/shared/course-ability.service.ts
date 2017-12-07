import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {AppConstants} from "../app.constants";
import {Course} from '../models/course.model';
import {Principal} from "./auth/principal-storage.service";

@Injectable()
export class CourseAbility {
  private principal: Principal;

  canAddStudents(course: Course): boolean {
    return this.hasCreateUpdateDeleteAuthority(course);
  }

  canEditGrade(course: Course): boolean {
    return this.hasCreateUpdateDeleteAuthority(course);
  }

  canDelete(course: Course): boolean {
    return this.hasCreateUpdateDeleteAuthority(course);
  }

  private hasCreateUpdateDeleteAuthority(course: Course): boolean {
    return !this.isCurrentUserAStudent() &&
      (this.isCurrentUserAdmin() ||
        this.isCurrentUserAdminInSameOrg(course) ||
        this.isCurrentUserCourseInstructor(course));
  }

  private isCurrentUserAStudent(): boolean {
    return this.principal.hasAuthority(AppConstants.Role.Student.name);
  }

  private isCurrentUserAdmin(): boolean {
    return this.principal.hasAuthority(AppConstants.Role.Admin.name);
  }

  private isCurrentUserAdminInSameOrg(course: Course): boolean {
    return this.principal.hasAuthority(AppConstants.Role.OrgAdmin.name) &&
      course.instructor &&
      this.principal.inOrganization(course.instructor.organizationId);
  }

  private isCurrentUserCourseInstructor(course: Course): boolean {
    return this.principal.isUser(course.instructorId);
  }
}
