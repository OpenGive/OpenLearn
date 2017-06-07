import {Component} from '@angular/core';
import {AdminGridModel} from "../../../../models/admin-grid.model";
import {AdminModel} from "../../admin.constants";

@Component({
  selector: 'app-admin-programs',
  template: '<app-admin-grid [grid]="programGrid"></app-admin-grid>',
})
export class AdminProgramsComponent {
  programGrid = new AdminGridModel(
    AdminModel.Program.title,
    AdminModel.Program.route,
    AdminModel.Program.columns,
    []);
}
