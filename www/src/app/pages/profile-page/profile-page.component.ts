import {Component, Input} from "@angular/core";
import {UserService} from "../../services/user.service";
import {AccountService} from "../../shared/auth/account.service";
import {Principal} from "../../shared/auth/principal.service";

@Component({
  selector: 'profile-form',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css', '../../controls/dialog-forms.css']
})
export class ProfilePageComponent {

  @Input() profile: any = {};
  @Input() editing: boolean;

  roles: string[];

  constructor(private userService: UserService, private accountService: AccountService, private principal: Principal) {
    this.accountService.get().subscribe(resp => {
      this.profile = resp;
      this.adjustRoles(this.profile.authorities);
    });
  }

  edit(): void {
    this.editing = true;
  }

  cancel(exit: boolean): void {
    this.editing = false;
    this.accountService.get().subscribe(resp => {this.profile = resp});
  }

  save(): void {
    this.userService.update(this.profile).subscribe(resp => {
      this.handleSaveResponse(resp);
      this.principal.authenticate(resp);
    });
  }

  handleSaveResponse(resp): void {
    this.editing = false;
  }

  ageAllowed(): boolean {
    return this.profile['14Plus'];
  }

  adjustRoles(roles: string[]): void {
    this.roles = roles.map(val => {
      return val.split('_').slice(1).map(str => str.charAt(0) + str.slice(1).toLowerCase()).join(' ');
    });
  }

}
