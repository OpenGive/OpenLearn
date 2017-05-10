import { Injectable, Inject } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import {StateStorageService} from "./state-storage.service";

@Injectable()
export class AuthServerProvider {

    constructor(
        private http: Http,
        private $localStorage: StateStorageService
    ) {}

    getToken() {
        return this.$localStorage.getToken();
    }

    login(credentials): Observable<any> {
        const data = 'username=' +  encodeURIComponent(credentials.username) + '&password=' +
            encodeURIComponent(credentials.password) + '&grant_type=password&scope=read%20write&' +
            'client_secret=my-secret-token-to-change-in-production&client_id=opengiveapp';
        const headers = new Headers ({
            'Content-Type': 'application/x-www-form-urlencoded',
            'Accept': 'application/json',
            'Authorization': 'Basic ' + btoa('opengiveapp' + ':' + 'my-secret-token-to-change-in-production')
        });

        return this.http.post('oauth/token', data, {
            headers
        }).map(authSuccess.bind(this));

        function authSuccess(resp) {
            const response = resp.json();
            const expiredAt = new Date();
            expiredAt.setSeconds(expiredAt.getSeconds() + response.expires_in);
            response.expires_at = expiredAt.getTime();
            this.$localStorage.store('authenticationToken', response);
            return response;
        }
    }

    logout(): Observable<any> {
        return new Observable((observer) => {
            this.http.post('api/logout', {});
            this.$localStorage.clearToken();
            observer.complete();
        });
    }
}
