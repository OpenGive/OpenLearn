import { Observable } from 'rxjs';
import { Http, RequestOptionsArgs, Headers } from '@angular/http';
import { CookieService } from 'ngx-cookie';
import { Injectable } from '@angular/core';

@Injectable()
export class HttpWrapperService {
    constructor(private _http: Http, private _cookieService: CookieService) {}

    get(endpoint: string) : Observable<any> {
        return this._http.get(endpoint, this.getAuthHeader());
    }

    post(endpoint: string, body: any) : Observable<any> {
        return this._http.post(endpoint, body, this.getAuthHeader());
    }

    put(endpoint: string, body: any) : Observable<any> {
        return this._http.put(endpoint, body, this.getAuthHeader());
    }

    private getAuthHeader(): RequestOptionsArgs {
        let toReturn: RequestOptionsArgs = { headers: new Headers() };
        let tokenObject = this._cookieService.getObject('token') as any;
        toReturn.headers.append('Authorization', 'Bearer ' + tokenObject.access_token);
        return toReturn;
    }
}