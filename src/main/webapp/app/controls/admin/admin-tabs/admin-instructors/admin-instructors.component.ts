import {Component} from '@angular/core';
import {AdminGridModel} from "../../../../models/admin-grid.model";
import {AdminModel} from "../../admin.constants";

@Component({
  selector: 'app-admin-instructors',
  template: '<app-admin-grid [grid]="instructorGrid"></app-admin-grid>'
})
export class AdminInstructorsComponent {
  instructorGrid = new AdminGridModel(
    AdminModel.Instructor.title,
    AdminModel.Instructor.route,
    AdminModel.Instructor.defaultSort,
    AdminModel.Instructor.columns,
    []);
}
