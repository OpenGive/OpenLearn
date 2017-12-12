import {Injectable} from '@angular/core';
import {Http, Headers} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {StateStorageService} from "./state-storage.service";
import {environment} from "../../../environments/environment";

@Injectable()
export class AuthServerProvider {

  private backend = environment.backend;

  constructor(private http: Http,
              private $localStorage: StateStorageService) {}

  login(credentials): Observable<any> {
    const data = 'username=' + encodeURIComponent(credentials.username) +
      '&password=' + encodeURIComponent(credentials.password) +
      '&grant_type=password' +
      '&scope=read%20write' +
      '&client_secret=my-secret-token-to-change-in-production' +
      '&client_id=openlearnapp';
    const headers = new Headers({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Accept': 'application/json',
      'Authorization': 'Basic ' + btoa('openlearnapp' + ':' + 'my-secret-token-to-change-in-production'),
      'ReCaptcha-Response': encodeURIComponent(credentials.reCaptcha)
    });

    return this.http.post(this.backend + '/oauth/token', data, {
      headers
    }).map(authSuccess.bind(this));

    function authSuccess(resp) {
      const response = resp.json();
      const expiredAt = new Date();
      expiredAt.setSeconds(expiredAt.getSeconds() + response.expires_in);
      response.expires_at = expiredAt.getTime();
      this.$localStorage.storeToken(response);
      console.log(response);
      return response;
    }
  }

  logout(): Observable<any> {
    this.$localStorage.clearToken();
    return this.http.post(this.backend + '/api/logout', {});
  }
}
