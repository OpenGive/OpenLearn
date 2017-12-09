import {Observable} from "rxjs";

import {Http, RequestOptionsArgs, Headers, Response} from "@angular/http";
import {Injectable} from "@angular/core";
import {Router} from "@angular/router";

import {environment} from "../../../environments/environment";
import {StateStorageService} from "./state-storage.service";
import {LogoutService} from "../../services/logout.service";
import {NotifyService} from "../../services/notify.service";

@Injectable()
export class HttpWrapperService {

    private backend = environment.backend;

    constructor(private _http: Http,
                private router: Router,
                private logoutService: LogoutService,
                private stateStorageService: StateStorageService,
                private notify: NotifyService) {}

    get(endpoint: string) : Observable<any> {
        return this._http.get(this.backend + endpoint, this.getAuthHeader()).catch(response => this.handleError(response));
    }

    getWithOptions(endpoint: string, options: any) : Observable<any> {
        let authHeaders = this.getAuthHeader();
        let combinedOptions = Object.assign(options, authHeaders);
        return this._http.get(this.backend + endpoint, combinedOptions).catch(response => this.handleError(response));
    }

    post(endpoint: string, body: any) : Observable<any> {
        return this._http.post(this.backend + endpoint, body, this.getAuthHeader()).catch(response => this.handleError(response));
    }

    put(endpoint: string, body: any) : Observable<any> {
        return this._http.put(this.backend + endpoint, body, this.getAuthHeader()).catch(response => this.handleError(response));
    }

    delete(endpoint: string) : Observable<any> {
        return this._http.delete(this.backend + endpoint, this.getAuthHeader()).catch(response => this.handleError(response));
    }

    private handleError(response: Response) {
        if (response.status === 401) {
            this.logoutService.logout();
            this.router.navigate(['/login']);
            this.notify.error('You have been automatically signed out. Please sign in again.')
        }
        return Observable.throw(new Error('Token has expired. Logging the user out now.'));
    }

    private getAuthHeader(): RequestOptionsArgs {
        const requestOptions: RequestOptionsArgs = { headers: new Headers() };
        const tokenObject = this.stateStorageService.getToken();
        if (tokenObject) {
            requestOptions.headers.append('Authorization', 'Bearer ' + tokenObject.access_token);
        }
        return requestOptions;
    }

}
