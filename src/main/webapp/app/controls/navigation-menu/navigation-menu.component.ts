import {Component, OnChanges, OnInit} from '@angular/core';
import {LoginPageComponent} from "../../pages/login-page/login-page.component";
import {LoginService} from "../../shared/login/login.service";

import {Principal} from "../../shared/auth/principal.service";

@Component({
  selector: 'app-navigation-menu',
  templateUrl: './navigation-menu.component.html',
  styleUrls: ['./navigation-menu.component.css']
})
export class NavigationMenuComponent implements OnChanges {

  name: string;

  constructor(private loginService: LoginService, private principal: Principal) {}

  ngOnChanges(): void {
    this.name = this.principal.getName();
  }

  authenticated(): boolean {
    let authenticated = this.principal.isIdentityResolved();
    this.ngOnChanges();
    return authenticated;
  }

  logout(): void {
    this.loginService.logout();
  }
}
