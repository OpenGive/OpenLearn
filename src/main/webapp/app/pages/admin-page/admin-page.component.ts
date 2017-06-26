import {Component, OnInit} from "@angular/core";

import {AdminModel} from "../../controls/admin/admin.constants";
import {Principal} from "../../shared/auth/principal.service";

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {

  tabs = [
    { name: AdminModel.Administrator.title, route: AdminModel.Administrator.route, authorities: AdminModel.Administrator.authorities },
    { name: AdminModel.Instructor.title, route: AdminModel.Instructor.route, authorities: AdminModel.Instructor.authorities },
    { name: AdminModel.Student.title, route: AdminModel.Student.route, authorities: AdminModel.Student.authorities },
    { name: AdminModel.Organization.title, route: AdminModel.Organization.route, authorities: AdminModel.Organization.authorities },
    { name: AdminModel.Session.title, route: AdminModel.Session.route, authorities: AdminModel.Session.authorities },
    { name: AdminModel.Program.title, route: AdminModel.Program.route, authorities: AdminModel.Program.authorities },
    { name: AdminModel.Course.title, route: AdminModel.Course.route, authorities: AdminModel.Course.authorities },
    // { name: AdminModel.Milestone.title, route: AdminModel.Milestone.route, authorities: AdminModel.Milestone.authorities },
    // { name: AdminModel.Achievement.title, route: AdminModel.Achievement.route, authorities: AdminModel.Achievement.authorities }
  ];
  filteredTabs: any[];

  constructor(private principal: Principal) {}

  ngOnInit(): void {
    this.filterTabs();
  }

  private filterTabs(): void {
    this.filteredTabs = [];
    this.tabs.forEach(tab => {
      this.principal.hasAnyAuthority(tab.authorities).then(resp => {
        if (resp) {
          this.filteredTabs.push(tab);
        }
      });
    });
  }
}
