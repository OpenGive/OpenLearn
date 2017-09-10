import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";

import {AdminService} from "./admin.service";

@Injectable()
export class AdminGridService {

  constructor(private adminService: AdminService) {}

  query(type: string): Observable<any> {
    return this.adminService.getAll(type);
  }

  create(toCreate: any, type: string): Observable<any> {
    return this.adminService.create(type, toCreate);
  }

  update(toUpdate: any, type: string): Observable<any> {
    return this.adminService.update(type, toUpdate);
  }
}
