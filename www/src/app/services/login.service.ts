import {Router} from '@angular/router';
import {Injectable} from '@angular/core';

import {Principal} from '../shared/auth/principal.service';
import {AuthServerProvider} from '../shared/auth/auth-oauth2.service';
import {AppConstants} from "../app.constants";

@Injectable()
export class LoginService {

  constructor(private principal: Principal,
              private authServerProvider: AuthServerProvider,
              private router: Router) {
  }

  login(credentials, callback?) {
    const cb = callback || function () {};

    return new Promise((resolve, reject) => {
      this.authServerProvider.login(credentials).subscribe((data) => {
        this.principal.identity(true).then((account) => {
          resolve(data);
          let home = 'access-denied';
          switch (account.authority) {
            case AppConstants.Role.Admin.name:
              home = AppConstants.Role.Admin.home;
              break;
            case AppConstants.Role.OrgAdmin.name:
              home = AppConstants.Role.OrgAdmin.home;
              break;
            case AppConstants.Role.Instructor.name:
              home = AppConstants.Role.Instructor.home;
              break;
            case AppConstants.Role.Student.name:
              home = AppConstants.Role.Student.home;
              break;
          }
          this.router.navigate([home]);
        });
        return cb();
      }, (err) => {
        this.logout();
        reject(err);
        return cb(err);
      });
    });
  }

  logout() {
    this.authServerProvider.logout().subscribe();
    this.principal.authenticate(null);
  }
}
