import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import * as _ from "lodash";

import {AppConstants} from "../app.constants";
import {HttpWrapperService} from '../shared/auth/http-wrapper.service';
import {User} from "../models/user.model";
import {Course} from "../models/course";
import {Principal} from "../shared/auth/principal.service";


@Injectable()
export class UserService {

  private endpoint = '/api/users';



  constructor(private _http: HttpWrapperService, private principal: Principal) {}

  getAll(): Observable<User[]> {
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

  getCoursesInstructedByUser(): Observable<Course[]> {
    return this._http.get(this.endpoint + '/' + this.principal.getLogin()+ '/instructors')
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
