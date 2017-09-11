import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminTabs} from "../admin.constants";

@Component({
  selector: 'app-admin-courses',
  template: '<app-admin-grid [grid]="courseGrid"></app-admin-grid>',
})
export class AdminCoursesComponent {
  courseGrid = new AdminGridModel(
    AdminTabs.Course.title,
    AdminTabs.Course.route,
    AdminTabs.Course.defaultSort,
    AdminTabs.Course.columns,
    []);
}
