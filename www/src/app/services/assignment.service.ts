import {Injectable} from "@angular/core";
import {Observable} from "rxjs";

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
}
