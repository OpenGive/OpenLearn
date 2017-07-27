import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminModel} from "../admin.constants";

@Component({
  selector: 'app-admin-sessions',
  template: '<app-admin-grid [grid]="sessionGrid"></app-admin-grid>',
})
export class AdminSessionsComponent {
  sessionGrid = new AdminGridModel(
    AdminModel.Session.title,
    AdminModel.Session.route,
    AdminModel.Session.defaultSort,
    AdminModel.Session.columns,
    []);
}
