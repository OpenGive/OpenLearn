import {Component} from '@angular/core';
import {AdminGridModel} from "../../../../models/admin-grid.model";
import {AdminModel} from "../../admin.constants";

@Component({
  selector: 'app-admin-students',
  template: '<app-admin-grid [grid]="studentGrid"></app-admin-grid>'
})
export class AdminStudentsComponent {
  studentGrid = new AdminGridModel(
    AdminModel.Student.title,
    AdminModel.Student.route,
    AdminModel.Student.columns,
    []);
}
