import {Component} from '@angular/core';
import {AdminGridModel} from "../../../../models/admin-grid.model";
import {AdminModel} from "../../admin.constants";

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent {
  userGrid = new AdminGridModel(AdminModel.User.title, AdminModel.User.columns, AdminModel.User.details, []);;
}
