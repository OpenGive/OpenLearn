import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";

import {AdminModel} from "../../controls/admin/admin.constants";
import {Principal} from "../../shared/auth/principal.service";

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {

  tabs = [
    { name: AdminModel.Organization.title, route: AdminModel.Organization.route, active: false, authorities: AdminModel.Organization.authorities },
    { name: AdminModel.Administrator.title, route: AdminModel.Administrator.route, active: false, authorities: AdminModel.Administrator.authorities },
    { name: AdminModel.Instructor.title, route: AdminModel.Instructor.route, active: false, authorities: AdminModel.Instructor.authorities },
    { name: AdminModel.Student.title, route: AdminModel.Student.route, active: false, authorities: AdminModel.Student.authorities },
    { name: AdminModel.Session.title, route: AdminModel.Session.route, active: false, authorities: AdminModel.Session.authorities },
    { name: AdminModel.Program.title, route: AdminModel.Program.route, active: false, authorities: AdminModel.Program.authorities },
    { name: AdminModel.Course.title, route: AdminModel.Course.route, active: false, authorities: AdminModel.Course.authorities },
    { name: AdminModel.Milestone.title, route: AdminModel.Milestone.route, active: false, authorities: AdminModel.Milestone.authorities }
    { name: AdminModel.Achievement.title, route: AdminModel.Achievement.route, active: false, authorities: AdminModel.Achievement.authorities }
  ];
  filteredTabs: any[];

  adminPageFlex = {
    lg: '1280px',
    xs: '100%'
  };

  constructor(private router: Router, private principal: Principal) {}

  ngOnInit(): void {
    this.filterTabs();
    this.readTabFromUrl();
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

  private readTabFromUrl(): void {
    this.selectTab(this.tabs.find(tab => tab.route === this.router.url.split('/').pop()));
  }

  selectTab(tab: any): void {
    this.tabs.forEach(tab => tab.active = false);
    tab.active = true;
  }
}
