import {Injectable} from "@angular/core";
import {Router} from "@angular/router"

import {Course} from "../models/course";
import {User} from "../models/user.model";
import {AdminService} from "./admin.service";
import {AdminModel} from "../controls/admin/admin.constants";

@Injectable()
export class DataService {
  private course: Course;
  private student: User;

  constructor(private router: Router,
              private adminService: AdminService) {
  }

  public getCourse(): Course {
    return this.course;
  }

  public getStudent(): User {
    return this.student;
  }

  public setCourse(course: Course) {
    this.course = course;
  }

  public setCourseById(id: Number) {
    this.adminService.get(AdminModel.Course.route, id).subscribe(resp => {
      this.course = resp;
      this.router.navigate(['/course'])
    });
  }

  public setStudentById(id: Number) {
    this.adminService.get(AdminModel.Student.route, id).subscribe(resp => {
      this.student = resp;
      this.router.navigate(['/student'])
    });
  }
}
