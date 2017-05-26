import { HttpWrapperService } from './../shared/auth/http-wrapper.service';
import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs";
import {log} from "util";
import {User} from "../shared/user/user.model";
import {Role} from "../app.constants";

@Injectable()
export class UserService {

  private endpoint = '/api/users';

  constructor(private _http: HttpWrapperService) {}

  getAllUsers() {
    return this._http.get(this.endpoint).map(resp => resp.json());
  }

  getAdminUsers() {
    return this._http.get(this.endpoint).map(resp => resp.json().filter(user => user.authorities.includes(Role.Admin)));
  }

  create(user: User): Observable<Response> {
    return this._http.post(this.endpoint, user).map(resp => resp.json());
  }

  update(user: User): Observable<Response> {
    return this._http.put(this.endpoint, user).map(resp => resp.json());
  }

  // authenticate(username: string, password: string): Observable<string> {
  //   let body = {
  //     username: username,
  //     password: password
  //   };

  //   return this._http.post("http://localhost:8080/oauth/authorize", body).map(s => {
  //     console.log(s.status);
  //     console.log(s.json());
  //     return s.json().bearerToken;
  //   }
  // );
  // }
}
