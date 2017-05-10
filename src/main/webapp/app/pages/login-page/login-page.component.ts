import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  username:string;
  password:string;

  constructor(private _userService:UserService) { }

  ngOnInit() {
  }

  flexSettings = {
    xs: '90%',
    sm: '400px'
  };

  login() {
    this._userService.authenticate(this.username, this.password)
      .subscribe(bearerToken => {

    });
  }
}
