import {Component, Input} from "@angular/core";
import {UserService} from "../../services/user.service";
import {AccountService} from "../../shared/auth/account.service";
import {Principal} from "../../shared/auth/principal-storage.service";
import {Account} from "../../models/account.model";
import {AdminService} from "../../services/admin.service";
import {AdminTabs} from "../../controls/admin/admin.constants";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'profile-form',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent {

  @Input() profile: any = {};

  item: Account;
  organizations: any[];
  administratorForm: FormGroup;
  editing: boolean = false;

  constructor(private userService: UserService,
              private accountService: AccountService,
              private adminService: AdminService,
              private principal: Principal) {

    this.item = this.principal.getUserIdentity();
    this.adminService.getAll(AdminTabs.Organization.route);
    this.accountService.get().subscribe(resp => {this.profile = resp});
    this.administratorForm = new FormGroup({});
  }


  // TODO: Convert this to a 'status' of EDITING, ADDING, VIEWING, etc
  updateEditing(isEditing: boolean): void {
    this.editing = isEditing;
  };

}