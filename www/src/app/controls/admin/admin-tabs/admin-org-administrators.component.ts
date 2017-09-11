import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminTabs} from "../admin.constants";

@Component({
  selector: 'app-org-admin-administrators',
  template: '<app-admin-grid [grid]="orgAdministratorGrid"></app-admin-grid>',
})
export class AdminOrgAdministratorsComponent {
  orgAdministratorGrid = new AdminGridModel(
    AdminTabs.OrgAdministrator.title,
    AdminTabs.OrgAdministrator.route,
    AdminTabs.OrgAdministrator.defaultSort,
    AdminTabs.OrgAdministrator.columns,
    []);
}
