import {Component, OnInit, ViewChild} from "@angular/core";
import {MdDialog} from "@angular/material";

import {RecaptchaComponent} from "ng-recaptcha";

import {LoginService} from "../../services/login.service";
import {NotifyService} from "../../services/notify.service";
import {ForgotPasswordDialogComponent} from "../../controls/forgot-password-dialog/forgot-password-dialog.component";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  @ViewChild('captchaRef') reCaptchaComponent: RecaptchaComponent;

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
    this.reCaptchaSiteKey = environment.reCaptchaSiteKey;
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
    }).catch((error) => {
      this.authenticationError = true;

      if (error.json().error === 'invalid_grant') {
        this.notify.error('Incorrect username or password');
      } else {
        this.notify.error('We encountered an error while trying to sign you in. Please try again.');
      }

      this.reCaptchaComponent.reset();
      this.failureCount++;
      if (this.failureCount > 2) {
        this.isOverLimit = true;
      }
    });
  }
}
