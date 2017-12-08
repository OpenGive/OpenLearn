import {Injectable} from '@angular/core';

import {AuthServerProvider} from '../shared/auth/auth-oauth2.service';
import {Principal} from "../shared/auth/principal-storage.service";

@Injectable()
export class LogoutService {

  constructor(private principal: Principal,
              private authServerProvider: AuthServerProvider) {
  }

  logout() {
    this.authServerProvider.logout().subscribe();
    this.principal.authenticate(null);
  }

}
