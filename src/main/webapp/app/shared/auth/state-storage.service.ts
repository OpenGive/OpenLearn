import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie';

@Injectable()
export class StateStorageService {
    constructor(
        private $sessionStorage: CookieService
    ) {}

    getPreviousState() {
        return this.$sessionStorage.getObject('previousState');
    }

    resetPreviousState() {
        this.$sessionStorage.remove('previousState');
    }

    storePreviousState(previousStateName, previousStateParams) {
        const previousState = { 'name': previousStateName, 'params': previousStateParams };
        this.$sessionStorage.putObject('previousState', previousState);
    }

    getDestinationState() {
        return this.$sessionStorage.getObject('destinationState');
    }

    storeToken(url: string) {
        this.$sessionStorage.put('bearerToken', url);
    }

    getToken() {
        return this.$sessionStorage.get('bearerToken');
    }

    clearToken() {
      this.$sessionStorage.remove('bearerToken');
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
        this.$sessionStorage.putObject('destinationState', destinationInfo);
    }
}
