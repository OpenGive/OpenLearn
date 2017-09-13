import {Injectable} from "@angular/core";
import {Router} from "@angular/router"

import {Course} from "../models/course.model";
import {User} from "../models/user.model";
import {AdminService} from "./admin.service";
import {AdminTabs} from "../controls/admin/admin.constants";
import {Student} from "../models/student.model";

@Injectable()
export class DataService {
  private course: Course;
  private student: Student;

  constructor(private router: Router,
              private adminService: AdminService) {
  }

  public getCourse(): Course {
    return this.course;
  }

  public getStudent(): Student {
    return this.student;
  }

  public setCourse(course: Course) {
    this.course = course;
  }

  public setCourseById(id: Number) {
    this.adminService.get(AdminTabs.Course.route, id).subscribe(resp => {
      this.course = resp;
      this.router.navigate(['/course'])
    });
  }

  public setStudentById(id: Number) {
    this.adminService.get(AdminTabs.Student.route, id).subscribe(resp => {
      this.student = resp;
      this.router.navigate(['/student'])
    });
  }
}
