import {Component} from '@angular/core';
import {AdminGridModel} from "../../../../models/admin-grid.model";
import {AdminModel} from "../../admin.constants";

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent {
  adminUserGrid = new AdminGridModel(
    AdminModel.AdminUser.title,
    AdminModel.AdminUser.columns,
    AdminModel.AdminUser.details,
    []);
}
