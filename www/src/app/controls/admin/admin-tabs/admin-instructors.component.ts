import {Component} from '@angular/core';
import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminTabs} from "../admin.constants";

@Component({
  selector: 'app-admin-instructors',
  template: '<app-admin-grid [grid]="instructorGrid"></app-admin-grid>'
})
export class AdminInstructorsComponent {
  instructorGrid = new AdminGridModel(
    AdminTabs.Instructor.title,
    AdminTabs.Instructor.route,
    AdminTabs.Instructor.defaultSort,
    AdminTabs.Instructor.columns,
    []);
}
