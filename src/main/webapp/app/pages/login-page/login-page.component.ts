import { AuthenticationService } from './../../shared/authentication/authentication.service';
import { Component, OnInit } from '@angular/core';

export interface User {
  email?: string;
  password?: string;
}

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  constructor(private _authService: AuthenticationService) { }

  ngOnInit() {
  }

  user : User = {
     email: "",
     password: ""
  }
 
  login() {
    this._authService.login(this.user.email, this.user.password);
  }

}


