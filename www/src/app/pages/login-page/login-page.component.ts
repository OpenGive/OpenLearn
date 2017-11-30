import {Component, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";

import {LoginService} from "../../services/login.service";
import {NotifyService} from "../../services/notify.service";
import {ForgotPasswordDialogComponent} from "../../controls/forgot-password-dialog/forgot-password-dialog.component";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  authenticationError: boolean;
  reCaptchaSiteKey: string;
  password: string;
  rememberMe: boolean;
  username: string;
  credentials: any;
  failureCount = 0;
  isOverLimit = false;

  constructor(private loginService: LoginService,
              private notify: NotifyService,
              private dialog: MdDialog) {
    this.credentials = {};
    this.reCaptchaSiteKey = '6LcIyDgUAAAAAGc7KQgXwlH8q17CR00ndcsXuEmN'; // TODO: Move to config
  }

  ngOnInit() {
  }

  flexSettings = {
    xs: '90%',
    sm: '400px'
  };

  forgotPassword() {
    this.dialog.open(ForgotPasswordDialogComponent);
  }

  login(reCaptchaResponse: string) {
    this.loginService.login({
      reCaptcha: reCaptchaResponse,
      username: this.username,
      password: this.password,
      rememberMe: this.rememberMe
    }).then(() => {
      this.authenticationError = false;
    }).catch(() => {
      this.authenticationError = true;
      this.notify.error('Incorrect username or password');
      this.failureCount++;
      if (this.failureCount > 2) {
        this.isOverLimit = true;
      }
    });
  }
}
