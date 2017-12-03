import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import { ResponseContentType, RequestOptions } from '@angular/http';

import {HttpWrapperService} from '../shared/auth/http-wrapper.service';
import {User} from "../models/user.model";

@Injectable()
export class AssignmentService {

  private endpoint = '/api/assignments';

  constructor(private _http: HttpWrapperService) {}

  getAssignmentsByCourse(courseId: Number): Observable<any[]> {
    return this._http.get(this.endpoint + '/course/' + courseId)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  private handleError(error: Response) {
    console.error(error);
    return Observable.throw(error.json() || {message: 'Server Error'});
  }

  getAssignmentStudentByAssignmentId(assignmentId: Number): Observable<any> {
    return this._http.get('/api/student-assignments/assignment/' + assignmentId)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getAssignmentByCourseAndStudent(courseId: Number, studentId: Number): Observable<any> {
    return this._http.get('/api/student-assignments/student/' + studentId + '/course/' + courseId)
      .map(resp => resp.json())
      .catch(this.handleError)
  }

  getAssignmentFiles(assignmentId: Number): Observable<any[]> {
    return this._http.get('/api/assignments/' + assignmentId + '/uploads')
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  deleteAssignmentFile(assignmentId: Number, keyName: String): Observable<any[]> {
    return this._http.delete('/api/assignments/' + assignmentId + '/upload/' + keyName)
      .catch(this.handleError);
  }

  getAssignmentFile(assignmentId: Number, keyName: String): Observable<Blob> {
    let options = new RequestOptions({responseType: ResponseContentType.Blob });
    return this._http.getWithOptions('/api/assignments/' + assignmentId + '/upload/' + keyName, options)
      .map(res => res.blob())
      .catch(this.handleError);
  }
}
