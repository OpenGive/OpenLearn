import {Injectable} from "@angular/core";

import {AccountService} from "./account.service";
import {Account} from "../index";
import {Principal} from "./principal-storage.service";

@Injectable()
export class PrincipalService {

  constructor(private accountService: AccountService,
              private principal: Principal) {}

  identity(force?: boolean): Promise<Account> {
    if (!force && this.principal.isIdentityResolved()) {
      return Promise.resolve(this.principal.getUserIdentity());
    } else {
      return this.accountService.get().toPromise().then((account) => {
        this.principal.authenticate(account);
        return this.principal.getUserIdentity();
      }).catch(() => {
        this.principal.authenticate(null);
        return this.principal.getUserIdentity();
      });
    }
  }

}
