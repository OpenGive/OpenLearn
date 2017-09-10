import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {AccountService} from './account.service';
import {AppConstants} from "../../app.constants";
import {AccountModel} from "../index";

@Injectable()
export class Principal {
  private userIdentity: AccountModel;
  private authenticated = false;
  private authenticationState = new Subject<AccountModel>();

  constructor(private account: AccountService) {
  }

  authenticate(identity) {
    this.userIdentity = identity;
    this.authenticated = identity !== null;
    this.authenticationState.next(this.userIdentity);
  }

  hasAnyAuthority(authorities: string[]): Promise<boolean> {
    if (authorities.length === 0) {
      return Promise.resolve(true);
    }
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authority) {
      return Promise.resolve(false);
    }

    for (let i = 0; i < authorities.length; i++) {
      if (authorities[i] === this.userIdentity.authority) {
        return Promise.resolve(true);
      }
    }

    return Promise.resolve(false);
  }

  hasAtLeastAuthority(authority: string): Promise<boolean> {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authority) {
      return Promise.resolve(false);
    }

    let isAuth = this.getPointValue(this.userIdentity.authority) >= this.getPointValue(authority);
    return Promise.resolve(isAuth);
  }

  private getPointValue(authority: string) {
    switch(authority) {
      case AppConstants.Role.Admin: return 4;
      case AppConstants.Role.OrgAdmin: return 3;
      case AppConstants.Role.Instructor: return 2;
      case AppConstants.Role.Student: return 1;
    }
  }

  hasAuthority(authority: string): boolean {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authority) {
      return false;
    }

    return this.userIdentity.authority === authority;
  }

  identity(force?: boolean): Promise<AccountModel> {
    if (force === true) {
      this.userIdentity = undefined;
    }

    // check and see if we have retrieved the userIdentity data from the server.
    // if we have, reuse it by immediately resolving
    if (this.userIdentity) {
      return Promise.resolve(this.userIdentity);
    }

    // retrieve the userIdentity data from the server, update the identity object, and then resolve.
    return this.account.get().toPromise().then((account) => {
      console.log(account);
      if (account) {
        this.userIdentity = account;
        this.authenticated = true;
      } else {
        this.userIdentity = null;
        this.authenticated = false;
      }
      this.authenticationState.next(this.userIdentity);
      return this.userIdentity;
    }).catch((err) => {
      this.userIdentity = null;
      this.authenticated = false;
      this.authenticationState.next(this.userIdentity);
      return null;
    });
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }

  isIdentityResolved(): boolean {
    return this.userIdentity != undefined;
  }

  getAuthenticationState(): Observable<AccountModel> {
    return this.authenticationState.asObservable();
  }

  getLogin(): string {
    return this.isIdentityResolved() ? this.userIdentity.login : null;
  }

  getName(): string {
    return this.isIdentityResolved() ? this.userIdentity.lastName + ', ' + this.userIdentity.firstName : null;
  }

  getRole(): string {
    return this.isIdentityResolved() ? this.userIdentity.authority : null;
  }

  getId(): Number {
    return this.isIdentityResolved() ? this.userIdentity.id : null;
  }
}
