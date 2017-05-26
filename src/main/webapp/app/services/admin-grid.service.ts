import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";

import {AdminModel} from "../controls/admin/admin.constants";
import {UserService} from "./user.service";

@Injectable()
export class AdminGridService {
  constructor(private userService: UserService) {}

  query(type: string): Observable<any> {
    switch (type) {
      case AdminModel.AdminUser.title:
        return this.userService.getAdminUsers();
    }
  }

  create(toCreate: any, type: string): Observable<any> {
    switch (type) {
      case AdminModel.AdminUser.title:
        return this.userService.create(toCreate);
    }
  }

  update(toUpdate: any, type: string): any {
    switch (type) {
      case AdminModel.AdminUser.title:
        return this.userService.create(toUpdate);
    }
  }
}
