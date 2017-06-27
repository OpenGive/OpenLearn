import { HttpWrapperService } from './http-wrapper.service';
import { Injectable } from '@angular/core';
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class AccountService  {
    constructor(private http: HttpWrapperService) { }

    get(): Observable<any> {
        return this.http.get('api/account').map((res: Response) => res.json());
    }
}
