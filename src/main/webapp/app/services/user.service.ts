import { Http } from "@angular/http";
import { Injectable } from "@angular/core";

@Injectable()
export class UserService {

    constructor(private _http: Http) {
        
    }

    authenticate() {
        let body = {
            username: 'test',
            password: 'testPass'
        };
        return this._http.post('https://heroduksite/api/authenticate', body).map(x => x.json());
    }
}