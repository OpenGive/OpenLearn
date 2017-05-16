import { Course } from './../models/course';
import { HttpWrapperService } from './../shared/auth/http-wrapper.service';
import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs";
import {log} from "util";

@Injectable()
export class CourseService {
  constructor(private _http: HttpWrapperService) {
  }
  getAllCourses(): Observable<Course[]> {
    return this._http.get('/api/courses').map(resp => {
      let json = resp.json();
      return json as Course[];
    });
  }

}
