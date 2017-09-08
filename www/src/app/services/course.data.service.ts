import {Injectable} from "@angular/core";
import {Router} from "@angular/router"

import {Course} from "../models/course";
import {CourseService} from "./course.service";

@Injectable()
export class DataService {
  public course: Course;

  constructor(private courseService: CourseService,
              private router: Router) {}

  public setCourseByCourse(c: Course)
  {
    this.course = c;
  }

  public setCourseById(s: Number)
  {
    this.courseService.get(s).subscribe(resp => {
      this.course = resp;
      this.router.navigate(['/course'])
    });
  }
}
