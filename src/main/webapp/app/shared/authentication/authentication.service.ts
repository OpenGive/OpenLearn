import { Injectable } from "@angular/core";
@Injectable() 
export class AuthenticationService {
    private _isLoggedIn: boolean = false;
    login(username: string, password: string) {
        //return false;
        console.log(this._isLoggedIn)
        this._isLoggedIn = true;
    }

    isLoggedIn() {
        // TODO: Check if user is logged in
        return this._isLoggedIn;
    }
}