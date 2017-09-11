import {Component, OnInit} from "@angular/core";

import {AdminTabs} from "../../controls/admin/admin.constants";
import {Principal} from "../../shared/auth/principal.service";

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {

  tabs = [
    { name: AdminTabs.Administrator.title, route: AdminTabs.Administrator.route, authorities: AdminTabs.Administrator.authorities },
    { name: AdminTabs.OrgAdministrator.title, route: AdminTabs.OrgAdministrator.route, authorities: AdminTabs.OrgAdministrator.authorities },
    { name: AdminTabs.Instructor.title, route: AdminTabs.Instructor.route, authorities: AdminTabs.Instructor.authorities },
    { name: AdminTabs.Student.title, route: AdminTabs.Student.route, authorities: AdminTabs.Student.authorities },
    { name: AdminTabs.Organization.title, route: AdminTabs.Organization.route, authorities: AdminTabs.Organization.authorities },
    { name: AdminTabs.Program.title, route: AdminTabs.Program.route, authorities: AdminTabs.Program.authorities },
    { name: AdminTabs.Session.title, route: AdminTabs.Session.route, authorities: AdminTabs.Session.authorities },
    { name: AdminTabs.Course.title, route: AdminTabs.Course.route, authorities: AdminTabs.Course.authorities }
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
