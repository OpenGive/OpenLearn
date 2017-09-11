import {Component} from '@angular/core';

import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminTabs} from "../admin.constants";

@Component({
  selector: 'app-admin-students',
  template: '<app-admin-grid [grid]="studentGrid"></app-admin-grid>'
})
export class AdminStudentsComponent {
  studentGrid = new AdminGridModel(
    AdminTabs.Student.title,
    AdminTabs.Student.route,
    AdminTabs.Student.defaultSort,
    AdminTabs.Student.columns,
    []);
}
