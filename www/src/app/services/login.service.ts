import {Router} from '@angular/router';
import {Injectable} from '@angular/core';

import {AuthServerProvider} from '../shared/auth/auth-oauth2.service';
import {AppConstants} from "../app.constants";
import {PrincipalService} from "../shared/auth/principal.service";
import {LogoutService} from "./logout.service";

@Injectable()
export class LoginService {

  constructor(private principalService: PrincipalService,
              private authServerProvider: AuthServerProvider,
              private router: Router,
              private logoutService: LogoutService) {
  }

  login(credentials, callback?) {
    const cb = callback || function () {};

    return new Promise((resolve, reject) => {
      this.authServerProvider.login(credentials).subscribe((data) => {
        this.principalService.identity(true).then((account) => {
          resolve(data);

          let home;
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
            default:
              home = 'access-denied';
              break;
          }
          this.router.navigate([home]);
        });
        return cb();
      }, (err) => {
        this.logoutService.logout();
        reject(err);
        return cb(err);
      });
    });
  }

}
