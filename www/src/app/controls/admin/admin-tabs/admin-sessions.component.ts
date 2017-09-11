import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminTabs} from "../admin.constants";

@Component({
  selector: 'app-admin-sessions',
  template: '<app-admin-grid [grid]="sessionGrid"></app-admin-grid>',
})
export class AdminSessionsComponent {
  sessionGrid = new AdminGridModel(
    AdminTabs.Session.title,
    AdminTabs.Session.route,
    AdminTabs.Session.defaultSort,
    AdminTabs.Session.columns,
    []);
}
