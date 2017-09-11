import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminTabs} from "../admin.constants";

@Component({
  selector: 'app-admin-organizations',
  template: '<app-admin-grid [grid]="organizationGrid"></app-admin-grid>',
})
export class AdminOrganizationsComponent {
  organizationGrid = new AdminGridModel(
    AdminTabs.Organization.title,
    AdminTabs.Organization.route,
    AdminTabs.Organization.defaultSort,
    AdminTabs.Organization.columns,
    []);
}
