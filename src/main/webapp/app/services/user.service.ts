import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import * as _ from "lodash";

import {AppConstants} from "../app.constants";
import {HttpWrapperService} from '../shared/auth/http-wrapper.service';
import {User} from "../shared/user/user.model";


@Injectable()
export class UserService {

  private endpoint = '/api/users';

  constructor(private _http: HttpWrapperService) {}

  getAll(): Observable<User[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json()
        .map(user => user))
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

  get(id: Number): Observable<User> {
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

  private handleError(error: Response) {
    console.error(error);
    return Observable.throw(error.json() || {message: 'Server Error'});
  }

  // Converts empty strings and empty address to nulls
  private nullifyBlanks(user: User) {
    if (_.every(user.address, field => _.isNil(field))) {
      user.address = null;
    }
    return _.mapValues(user, field => {
      if (field === '') {
        field = null;
      }
      return field;
    });
  }
}
