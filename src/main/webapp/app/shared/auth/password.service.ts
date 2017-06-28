import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';

import {AccountService} from "./account.service";

@Injectable()
export class PasswordService {

  constructor(private accountService: AccountService) {}

  resetPassword(login: string): Observable<any> {
    return this.accountService.resetPassword(login);
  }
}
