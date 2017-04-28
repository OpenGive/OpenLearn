import { AuthenticationService } from './authentication.service';
import { Injectable } from "@angular/core";
import { CanActivate } from "@angular/router";

@Injectable()
export class IsLoggedInGuardService implements CanActivate {

    /**
     *
     */
    constructor(private authService: AuthenticationService) {
        

    }

    canActivate() {
        console.log('ACTIVATE');
        
        return this.authService.isLoggedIn();
    }
}