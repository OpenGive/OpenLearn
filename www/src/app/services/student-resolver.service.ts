import {Injectable} from "@angular/core";
import {Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot} from "@angular/router";

import {Observable} from 'rxjs/Observable';

import {Student} from "../models/student.model"
import {AdminService} from "./admin.service"
import {AdminTabs} from "../controls/admin/admin.constants";

@Injectable()
export class StudentResolver implements Resolve<Student> {

  constructor(private router: Router,
              private adminService: AdminService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Student> {
    const id = Number(route.paramMap.get('id'));

    return this.adminService.get(AdminTabs.Student.route, id).take(1).map(student => {
      if (student) {
        return student ;
      } else {
        this.router.navigate([AdminTabs.Student.route]);
        return null;
      }
    });
  }
}