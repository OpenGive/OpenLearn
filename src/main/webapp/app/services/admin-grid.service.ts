import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";

import {AdminModel} from "../controls/admin/admin.constants";
import {UserService} from "./user.service";

@Injectable()
export class AdminGridService {
  constructor(private userService: UserService) {}

  query(type: string): Observable<any> {
    switch (type) {
      case AdminModel.Administrator.title:
        return this.userService.getAdminUsers();
      case AdminModel.Instructor.title:
        return this.userService.getInstructors();
      case AdminModel.Student.title:
        return this.userService.getStudents();
    }
  }

  create(toCreate: any, type: string): Observable<any> {
    switch (type) {
      case AdminModel.Administrator.title || AdminModel.Instructor.title || AdminModel.Student.title:
        return this.userService.create(toCreate);

    }
  }

  update(toUpdate: any, type: string): any {
    switch (type) {
      case AdminModel.Administrator.title || AdminModel.Instructor.title || AdminModel.Student.title:
        return this.userService.create(toUpdate);
    }
  }
}
