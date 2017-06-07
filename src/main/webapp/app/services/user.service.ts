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
      .map(resp => resp.json()
        .map(user => this.flattenAddress(user)))
      .catch(this.handleError);
  }

  create(user: User): Observable<User> {
    return this._http.post(this.endpoint, this.buildAddress(user))
      .map(resp => this.flattenAddress(resp.json()))
      .catch(this.handleError);
  }

  update(user: User): Observable<User> {
    return this._http.put(this.endpoint, this.buildAddress(user))
      .map(resp => this.flattenAddress(resp.json()))
      .catch(this.handleError);
  }

  delete(id: Number) {
    return this._http.delete(this.endpoint + '/' + id)
      .map(resp => resp)
      .catch(this.handleError);
  }

  get(id: Number): Observable<User> {
    return this._http.get(this.endpoint + '/' + id)
      .map(resp => this.flattenAddress(resp.json()))
      .catch(this.handleError);
  }

  getAdministrators(): Observable<User[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json()
        .filter(user => user.authorities.includes(AppConstants.Role.Admin))
        .map(user => this.flattenAddress(user)))
      .catch(this.handleError);
  }

  getInstructors(): Observable<User[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json()
        .filter(user => user.authorities.includes(AppConstants.Role.Instructor))
        .map(user => this.flattenAddress(user)))
      .catch(this.handleError);
  }

  getStudents(): Observable<User[]> {
    return this._http.get(this.endpoint)
      .map(resp => console.log(resp.json()
        .filter(user => user.authorities.includes(AppConstants.Role.Student))
        .map(user => this.flattenAddress(user))))
      .catch(this.handleError);
  }

  private handleError(error: Response) {
    console.error(error);
    return Observable.throw(error.json() || 'Server Error');
  }

  private flattenAddress(user) {
    if (user.address) {
      user.streetAddress1 = user.address.streetAddress1;
      user.streetAddress2 = user.address.streetAddress2;
      user.city = user.address.city;
      user.state = user.address.state;
      user.postalCode = user.address.postalCode;
    }
    return user;
  }

  private buildAddress(user) {
    if (user.streetAddress1 || user.streetAddress2 || user.city || user.state || user.postalCode) {
      user.address = {
        streetAddress1: user.streetAddress1,
        streetAddress2: user.streetAddress2,
        city: user.city,
        state: user.state,
        postalCode: user.postalCode
      };
    }
    return user;
  }
}
