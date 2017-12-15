import {Component, Input} from "@angular/core";
import {AccountService} from "../../shared/auth/account.service";
import {Principal} from "../../shared/auth/principal-storage.service";

@Component({
  selector: 'profile-form',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css', '../../controls/dialog-forms.css']
})
export class ProfilePageComponent {

  @Input() profile: any = {};
  @Input() editing: boolean;

  constructor(private accountService: AccountService, private principal: Principal) {
    this.accountService.get().subscribe(resp => {this.profile = resp});
  }

  edit(): void {
    this.editing = true;
  }

  cancel(exit: boolean): void {
    this.editing = false;
    this.accountService.get().subscribe(resp => {this.profile = resp});
  }

  save(): void {
  }

  handleSaveResponse(resp): void {
    this.editing = false;
  }

  ageAllowed(): boolean {
    return this.profile['14Plus'];
  }

}
