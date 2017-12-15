import {Component, Input, OnInit} from "@angular/core";
import {UserService} from "../../services/user.service";
import {AccountService} from "../../shared/auth/account.service";
import {Principal} from "../../shared/auth/principal-storage.service";
import {Account} from "../../models/account.model";
import {AdminService} from "../../services/admin.service";
import {AdminTabs} from "../../controls/admin/admin.constants";
import {FormGroup} from "@angular/forms";
import {AppConstants} from "../../app.constants";

@Component({
  selector: 'profile-form',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {

  item: Account;
  organizations: any[];
  profileForm: FormGroup;
  editing: boolean = false;

  route: string;

  constructor(private userService: UserService,
              private accountService: AccountService,
              private adminService: AdminService,
              private principal: Principal) {

    this.profileForm = new FormGroup({});
  }

  ngOnInit(): void {
    switch (this.principal.getRole()) {
      case AppConstants.Role.Admin.name:
        this.route = AdminTabs.Administrator.route;
        break;
      case AppConstants.Role.OrgAdmin.name:
        this.route = AdminTabs.OrgAdministrator.route;
        break;
      case AppConstants.Role.Instructor.name:
        this.route = AdminTabs.Instructor.route;
        break;
    }

    this.adminService.get(this.route, this.principal.getId()).subscribe(resp => {
      this.item = resp;
    });
  }

  updateEditing(isEditing: boolean): void {
    this.editing = isEditing;
  };

  updated(): void {
    this.editing = false;
  }

}