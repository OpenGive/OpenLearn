import {Component} from '@angular/core';
import {AdminGridModel} from "../../../../models/admin-grid.model";
import {AdminModel} from "../../admin.constants";

@Component({
  selector: 'app-admin-organizations',
  template: '<app-admin-grid [grid]="organizationGrid"></app-admin-grid>',
})
export class AdminOrganizationsComponent {
  organizationGrid = new AdminGridModel(
    AdminModel.Organization.title,
    AdminModel.Organization.route,
    AdminModel.Organization.defaultSort,
    AdminModel.Organization.columns,
    []);
}
