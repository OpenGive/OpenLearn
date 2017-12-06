import {Injectable} from "@angular/core";
import {Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot} from "@angular/router";

import {Observable} from 'rxjs/Observable';

import {Course} from "../models/course.model"
import {AdminService} from "./admin.service"
import {AdminTabs} from "../controls/admin/admin.constants";

@Injectable()
export class CourseResolver implements Resolve<Course> {

  constructor(private router: Router,
              private adminService: AdminService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Course> {
    const id = Number(route.paramMap.get('id'));

    return this.adminService.get(AdminTabs.Course.route, id).take(1).map(course => {
      if (course) {
        return course;
      } else {
        this.router.navigate([AdminTabs.Course.route]);
        return null;
      }
    });
  }
}