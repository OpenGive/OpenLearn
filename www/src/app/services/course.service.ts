import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import * as _ from "lodash";

import {AppConstants} from "../app.constants";
import {HttpWrapperService} from '../shared/auth/http-wrapper.service';
import {User} from "../models/user.model";
import {Course} from "../models/course";


@Injectable()
export class CourseService {

  private endpoint = '/api/courses';

  constructor(private _http: HttpWrapperService) {}

  getAll(): Observable<Course[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  create(user: User): Observable<User> {
    return this._http.post(this.endpoint, this.nullifyBlanks(user))
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  update(user: User): Observable<User> {
    return this._http.put(this.endpoint, this.nullifyBlanks(user))
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  delete(id: Number) {
    return this._http.delete(this.endpoint + '/' + id)
      .map(resp => resp)
      .catch(this.handleError);
  }

  get(id: Number): Observable<Course> {
    return this._http.get(this.endpoint + '/' + id)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getAdministrators(): Observable<User[]> { // TODO: Move to backend
    return this._http.get(this.endpoint)
      .map(resp => resp.json()
        .filter(user => user.authorities.includes(AppConstants.Role.Admin) || user.authorities.includes(AppConstants.Role.OrgAdmin)))
      .catch(this.handleError);
  }

  getInstructors(): Observable<User[]> { // TODO: Move to backend
    return this._http.get(this.endpoint)
      .map(resp => resp.json()
        .filter(user => user.authorities.includes(AppConstants.Role.Instructor)))
      .catch(this.handleError);
  }

  getStudents(): Observable<User[]> { // TODO: Move to backend
    return this._http.get(this.endpoint)
      .map(resp => resp.json()
        .filter(user => user.authorities.includes(AppConstants.Role.Student)))
      .catch(this.handleError);
  }

  getCourseStudents(id: Number): Observable<User[]> {
    return this._http.get(this.endpoint + '/' + id + '/students')
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getStudentsCourses(id: Number): Observable<Course []> {
    return this._http.get(this.endpoint + '/' + id + '/coursesByStudent')
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getCourseStudentsNot(id: Number): Observable<User []> {
    return this._http.get(this.endpoint + '/' + id + '/studentsNot')
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  addStudentToCourse(courseId: Number, studentId: Number): Observable<User> {
    return this._http.post(this.endpoint + '/' + courseId + '/students?studentId=' + studentId, null)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  removeStudentFromCourse(courseId: Number, studentId: Number): Observable<User> {
    return this._http.delete(this.endpoint + '/' + courseId + '/students/' + studentId)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  setStudentGradeForCourse(courseId: Number, studentId: Number, grade: Number): Observable<User> {
    return this._http.post(this.endpoint + '/' + courseId + '/grade?studentId=' + studentId + '&grade=' + grade, null)
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

  private nullifyBlanks(user: User) {
    // Convert empty state to null
    if (user.address && user.address.state === '') {
      user.address.state = null;
    }
    // Convert empty address to null
    if (_.every(user.address, field => _.isNil(field))) {
      user.address = null;
    }
    // Convert empty strings to null
    return _.mapValues(user, field => {
      if (field === '') {
        field = null;
      }
      return field;
    });
  }
}
