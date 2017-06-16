import {Component} from '@angular/core';

import {AdminGridModel} from "../../../../models/admin-grid.model";
import {AdminModel} from "../../admin.constants";

@Component({
  selector: 'app-admin-administrators',
  template: '<app-admin-grid [grid]="achievementsGrid"></app-admin-grid>',
})
export class AdminAchievementsComponent {
  achievementsGrid = new AdminGridModel(
    AdminModel.Administrator.title,
    AdminModel.Administrator.route,
    AdminModel.Administrator.defaultSort,
    AdminModel.Administrator.columns,
    []);
}
