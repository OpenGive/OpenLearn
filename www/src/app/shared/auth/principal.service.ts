import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { AccountService } from './account.service';
import { AppConstants } from "../../app.constants";
import { AccountModel } from "../index";

@Injectable()
export class Principal {
    private userIdentity: AccountModel;
    private authenticated = false;
    private authenticationState = new Subject<AccountModel>();

    constructor(
        private account: AccountService
    ) {}

    authenticate(identity) {
        this.userIdentity = identity;
        this.authenticated = identity !== null;
        this.authenticationState.next(this.userIdentity);
    }

    hasAnyAuthority(authorities: string[]): Promise<boolean> {
        if (authorities.length === 0) {
            return Promise.resolve(true);
        }
        if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
            return Promise.resolve(false);
        }

        for (let i = 0; i < authorities.length; i++) {
            if (this.userIdentity.authorities.indexOf(authorities[i]) !== -1) {
                return Promise.resolve(true);
            }
        }

        return Promise.resolve(false);
    }

    collectionHasAtLeastAuthority(authorities: string[], authToCheck: string): boolean {

        var isAuth = false;

        switch (authToCheck) {
            case AppConstants.Role.Student:
                isAuth = isAuth || authorities.some(a => a === AppConstants.Role.Student);
            case AppConstants.Role.Instructor:
                isAuth = isAuth || authorities.some(a => a === AppConstants.Role.Instructor);
            case AppConstants.Role.OrgAdmin:
                isAuth = isAuth || authorities.some(a => a === AppConstants.Role.OrgAdmin);
            case AppConstants.Role.Admin:
                isAuth = isAuth || authorities.some(a => a === AppConstants.Role.Admin);
        }

        return isAuth;
    }

    hasAtLeastAuthority(authority: string): Promise<boolean> {
        if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
            return Promise.resolve(false);
        }

        let isAuth = this.collectionHasAtLeastAuthority(this.userIdentity.authorities, authority);
        return Promise.resolve(isAuth);
    }

    hasAuthority(authority: string): Promise<boolean> {
        if (!this.authenticated) {
           return Promise.resolve(false);
        }

        return this.identity().then((id) => {
            return Promise.resolve(id.authorities && id.authorities.indexOf(authority) !== -1);
        }, () => {
            return Promise.resolve(false);
        });
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

    getImageUrl(): string {
        return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
    }

    getLogin(): string {
        return this.isIdentityResolved() ? this.userIdentity.login : null;
    }

    getName(): string {
        return this.isIdentityResolved() ? this.userIdentity.lastName + ', ' + this.userIdentity.firstName : null;
    }

    getRoles(): string[] {
        return this.isIdentityResolved() ? this.userIdentity.authorities : [];
    }

    getId(): Number {
        return this.isIdentityResolved() ? this.userIdentity.id: null;
    }
}
