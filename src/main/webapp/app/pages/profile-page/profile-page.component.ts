import {Component, Input, OnInit} from "@angular/core";
import {UserService} from "../../services/user.service";
import {AccountService} from "../../shared/auth/account.service";

@Component({
  selector: 'profile-form',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {

  @Input() profile: any = {};
  @Input() editing: boolean;

  constructor(private userService: UserService, private accountService: AccountService) {
    this.accountService.get().subscribe(resp => {this.profile = resp});
  }

  ngOnInit() {

  }

  edit(): void {
    this.editing = true;
  }

  cancel(exit: boolean): void {
    this.editing = false;
    this.accountService.get().subscribe(resp => {this.profile = resp});
  }

  save(): void {
    this.userService.update(this.profile).subscribe(resp => this.handleSaveResponse(resp));
  }

  handleSaveResponse(resp): void {
    this.editing = false;
  }

  ageAllowed(): boolean {
    return this.profile['14Plus'];
  }

}
