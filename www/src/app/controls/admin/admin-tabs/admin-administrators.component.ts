import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminTabs} from "../admin.constants";

@Component({
  selector: 'app-admin-administrators',
  template: '<app-admin-grid [grid]="administratorGrid"></app-admin-grid>',
})
export class AdminAdministratorsComponent {
  administratorGrid = new AdminGridModel(
    AdminTabs.Administrator.title,
    AdminTabs.Administrator.route,
    AdminTabs.Administrator.defaultSort,
    AdminTabs.Administrator.columns,
    []);
}
