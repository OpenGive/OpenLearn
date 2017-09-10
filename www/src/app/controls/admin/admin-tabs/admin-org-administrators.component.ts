import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminModel} from "../admin.constants";

@Component({
  selector: 'app-org-admin-administrators',
  template: '<app-admin-grid [grid]="orgAdministratorGrid"></app-admin-grid>',
})
export class AdminOrgAdministratorsComponent {
  orgAdministratorGrid = new AdminGridModel(
    AdminModel.OrgAdministrator.title,
    AdminModel.OrgAdministrator.route,
    AdminModel.OrgAdministrator.defaultSort,
    AdminModel.OrgAdministrator.columns,
    []);
}
