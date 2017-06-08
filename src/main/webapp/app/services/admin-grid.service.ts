import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";

import {AdminModel} from "../controls/admin/admin.constants";
import {UserService} from "./user.service";
import {AdminService} from "./admin.service";

@Injectable()
export class AdminGridService {
  constructor(private userService: UserService, private adminService: AdminService) {}

  query(type: string): Observable<any> {
    switch (type) {
      case AdminModel.Administrator.route:
        return this.userService.getAdministrators();
      case AdminModel.Instructor.route:
        return this.userService.getInstructors();
      case AdminModel.Student.route:
        return this.userService.getStudents();
      default:
        return this.adminService.getAll(type);
    }
  }

  create(toCreate: any, type: string): Observable<any> {
    switch (type) {
      case AdminModel.Administrator.route || AdminModel.Instructor.route || AdminModel.Student.route:
        return this.userService.create(toCreate);
      default:
        return this.adminService.create(type, toCreate);
    }
  }

  update(toUpdate: any, type: string): any {
    switch (type) {
      case AdminModel.Administrator.route || AdminModel.Instructor.route || AdminModel.Student.route:
        return this.userService.update(toUpdate);
      default:
        return this.adminService.update(type, toUpdate);
    }
  }
}
