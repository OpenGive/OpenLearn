import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";

import {Principal} from "./principal-storage.service";
import {PrincipalService} from "./principal.service";

@Injectable()
export class UserRouteAccessService implements CanActivate {

    constructor(private router: Router,
                private principal: Principal,
                private principalService: PrincipalService/*,
                private stateStorageService: StateStorageService*/) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {

        const authorities = route.data['authorities'];
        // if (!authorities || authorities.length === 0) {
        //     if (route.data['authenticate'] == true) {
        //         return this.principal.isAuthenticated();
        //     }
        //     return true;
        // }

        return this.checkLogin(authorities, state.url);
    }

    checkLogin(authorities: string[], url: string): Promise<boolean> {
        const p = this.principalService.identity().then( (account) => {
            if (account) {
                return this.principal.hasAnyAuthority(authorities).then( (hasAuth) => {
                    if (hasAuth) {
                        return Promise.resolve(true);
                    }
                    console.log(authorities);
//                    this.stateStorageService.storeUrl(url);
                    this.router.navigate(['access-denied']);

                    return Promise.resolve(false);
                });
            }

            this.router.navigate(['/login']);
            return Promise.resolve(false);
        });
        return Promise.resolve(p);
    }
}
