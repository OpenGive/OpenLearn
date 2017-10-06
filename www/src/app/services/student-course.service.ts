import {Injectable} from "@angular/core";
import {Observable} from "rxjs";

import {HttpWrapperService} from '../shared/auth/http-wrapper.service';
import {User} from "../models/user.model";

@Injectable()
export class StudentCourseService {

  private endpoint = '/api/student-courses';

  constructor(private _http: HttpWrapperService) {}

  getStudentCoursesByStudent(studentId: Number): Observable<any[]> {
    return this._http.get(this.endpoint + '/student/' + studentId)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getStudentCoursesByCourse(courseId: Number): Observable<any[]> {
    return this._http.get(this.endpoint + '/course/' + courseId)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  createStudentCourse(studentId: Number, courseId: Number): Observable<any> {
    return this._http.post(this.endpoint, {
      studentId: studentId,
      courseId: courseId
    }).map(resp => resp.json())
      .catch(this.handleError);
  }

  updateStudentCourse(studentCourse: any): Observable<any> {
    return this._http.put(this.endpoint, studentCourse)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  deleteStudentCourse(studentCourseId: Number): Observable<void> {
    return this._http.delete(this.endpoint + '/' + studentCourseId)
      .catch(this.handleError);
  }

  getStudentsNotInCourse(courseId: Number): Observable<any[]> {
    return this._http.get('/api/students' + '/notInCourse/' + courseId)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getCourseStudentsNot(id: Number): Observable<User []> {
    return this._http.get(this.endpoint + '/' + id + '/studentsNot')
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getCourseResources(courseId: Number): Observable<User[]> {
    return this._http.get(this.endpoint + '/' + courseId + '/resources')
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  removeResourceFromCourse(courseId: Number, resourceId: Number): Observable<User> {
    return this._http.delete(this.endpoint + '/' + courseId + '/resources/' + resourceId)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  addResourceToCourse(courseId: Number, resourceId:Number): Observable<any> {
    return this._http.post(this.endpoint + '/' + courseId + '/resources?itemLinkId=' + resourceId, null)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  private handleError(error: Response) {
    console.error(error);
    return Observable.throw(error.json() || {message: 'Server Error'});
  }
}
