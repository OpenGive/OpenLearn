import {Injectable} from "@angular/core";
import {Router} from "@angular/router"

import {Course} from "../models/course";
import {User} from "../models/user.model";
import {CourseService} from "./course.service";
import {UserService} from "./user.service";

@Injectable()
export class DataService {
  public course: Course;
  private student: User;

  constructor(private courseService: CourseService,
              private router: Router,
              private userService: UserService) {
  }

  public setCourseByCourse(c: Course) {
    this.course = c;
  }

  public setCourseById(c: Number) {
    this.courseService.get(c).subscribe(resp => {
      this.course = resp;
      this.router.navigate(['/course'])
    });
  }

  public setStudentByLogin(login: String) {
    this.userService.get(login).subscribe(resp => {
      console.log("Got to service call");
      console.log(resp);
      this.student = resp;
      this.router.navigate(['/student'])
    })
  }

  public getStudent() {
    return this.student;
  }

}
