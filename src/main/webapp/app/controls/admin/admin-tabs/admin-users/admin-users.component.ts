import {Component, OnInit} from '@angular/core';
import {AdminGridModel} from "../../../../models/admin-grid.model";
import {AdminModel} from "../../admin.constants";
import {UserService} from "../../../../services/user.service";

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {

  userGrid: AdminGridModel;

  constructor(
    public userService: UserService
  ) { }

  getUsers(): void {
    this.userService.getAllUsers().subscribe(users => this.userGrid.rows = users);
  }

  ngOnInit() {
    this.getUsers();
    this.userGrid = new AdminGridModel(AdminModel.User.title, AdminModel.User.columns, AdminModel.User.details, []);
  }

}
