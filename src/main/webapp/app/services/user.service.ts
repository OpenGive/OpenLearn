import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs";
import {log} from "util";

@Injectable()
export class UserService {
  constructor(private _http: Http) {
  }

  authenticate(username: string, password: string): Observable<string> {
    let body = {
      username: username,
      password: password
    };

    return this._http.post("http://localhost:8080/oauth/authorize", body).map(s => {
      console.log(s.status);
      console.log(s.json());
      return s.json().bearerToken;
    }
  );
  }
}
