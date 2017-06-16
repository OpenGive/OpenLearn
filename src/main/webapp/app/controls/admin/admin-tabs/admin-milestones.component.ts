import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminModel} from "../admin.constants";

@Component({
  selector: 'app-admin-milestones',
  template: '<app-admin-grid [grid]="milestoneGrid"></app-admin-grid>',
})
export class AdminMilestonesComponent {
  milestoneGrid = new AdminGridModel(
    AdminModel.Milestone.title,
    AdminModel.Milestone.route,
    AdminModel.Milestone.defaultSort,
    AdminModel.Milestone.columns,
    []);
}
