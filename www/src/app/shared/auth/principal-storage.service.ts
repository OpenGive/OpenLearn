import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {AppConstants} from "../../app.constants";
import {Account} from "../index";

@Injectable()
export class Principal {
  private userIdentity: Account = null;
  private authenticated = false;
  private authenticationState = new Subject<Account>();

  constructor() {}

  authenticate(identity: Account) {
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
      case AppConstants.Role.Admin.name: return 4;
      case AppConstants.Role.OrgAdmin.name: return 3;
      case AppConstants.Role.Instructor.name: return 2;
      case AppConstants.Role.Student.name: return 1;
    }
  }

  hasAuthority(authority: string): boolean {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authority) {
      return false;
    }

    return this.userIdentity.authority === authority;
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }

  isIdentityResolved(): boolean {
    return this.userIdentity !== null;
  }

  getAuthenticationState(): Observable<Account> {
    return this.authenticationState.asObservable();
  }

  getUserIdentity(): Account {
    return this.userIdentity;
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
