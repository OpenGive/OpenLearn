import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminTabs} from "../admin.constants";

@Component({
  selector: 'app-admin-programs',
  template: '<app-admin-grid [grid]="programGrid"></app-admin-grid>',
})
export class AdminProgramsComponent {
  programGrid = new AdminGridModel(
    AdminTabs.Program.title,
    AdminTabs.Program.route,
    AdminTabs.Program.defaultSort,
    AdminTabs.Program.columns,
    []);
}
