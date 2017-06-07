import {HttpWrapperService} from '../shared/auth/http-wrapper.service';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {User} from "../shared/user/user.model";
import {AppConstants} from "../app.constants";

@Injectable()
export class UserService {

  private endpoint = '/api/users';

  constructor(private _http: HttpWrapperService) {}

  getAll(): Observable<User[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  create(user: User): Observable<User> {
    return this._http.post(this.endpoint, user)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  update(user: User): Observable<User> {
    return this._http.put(this.endpoint, user)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  delete(id: Number) {
    return this._http.delete(this.endpoint + '/' + id)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  get(id: Number): Observable<User> {
    return this._http.get(this.endpoint + '/' + id)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getAdministrators(): Observable<User[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json().filter(user => user.authorities.includes(AppConstants.Role.Admin)))
      .catch(this.handleError);
  }

  getInstructors(): Observable<User[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json().filter(user => user.authorities.includes(AppConstants.Role.Instructor)))
      .catch(this.handleError);
  }

  getStudents(): Observable<User[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json().filter(user => user.authorities.includes(AppConstants.Role.Student)))
      .catch(this.handleError);
  }

  private handleError(error: Response) {
    console.error(error);
    return Observable.throw(error.json() || 'Server Error');
  }
}
