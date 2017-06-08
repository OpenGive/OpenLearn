import {Component} from '@angular/core';
import {AdminGridModel} from "../../../../models/admin-grid.model";
import {AdminModel} from "../../admin.constants";

@Component({
  selector: 'app-admin-courses',
  template: '<app-admin-grid [grid]="courseGrid"></app-admin-grid>',
})
export class AdminCoursesComponent {
  courseGrid = new AdminGridModel(
    AdminModel.Course.title,
    AdminModel.Course.route,
    AdminModel.Course.columns,
    []);
}
