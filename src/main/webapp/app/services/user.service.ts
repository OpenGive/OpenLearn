import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs";

@Injectable()
export class UserService {
  constructor(private _http: Http) {
  }

  authenticate(username: string, password: string): Observable<string> {
    let body = {
      username: username,
      password: password
    };

    return this._http.post("/api/authenticate", body).map(s => s.json().bearerToken);
  }
}
