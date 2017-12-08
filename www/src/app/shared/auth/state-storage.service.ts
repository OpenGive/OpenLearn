import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie';

@Injectable()
export class StateStorageService {
    constructor(
        private cookieService: CookieService
    ) {}

    getPreviousState() {
        return this.cookieService.getObject('previousState');
    }

    resetPreviousState() {
        this.cookieService.remove('previousState');
    }

    storePreviousState(previousStateName, previousStateParams) {
        const previousState = { 'name': previousStateName, 'params': previousStateParams };
        this.cookieService.putObject('previousState', previousState);
    }

    getDestinationState() {
        return this.cookieService.getObject('destinationState');
    }

    storeToken(tokens: any) {
        this.cookieService.putObject('token', tokens);
    }

    getToken() {
        return this.cookieService.getObject('token') as any;
    }

    clearToken() {
      this.cookieService.remove('token');
    }

    storeDestinationState(destinationState, destinationStateParams, fromState) {
        const destinationInfo = {
            'destination': {
                'name': destinationState.name,
                'data': destinationState.data,
            },
            'params': destinationStateParams,
            'from': {
                'name': fromState.name,
             }
        };
        this.cookieService.putObject('destinationState', destinationInfo);
    }
}
