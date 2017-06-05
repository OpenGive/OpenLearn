import {Component, OnInit} from "@angular/core";
import {AdminModel} from "../../controls/admin/admin.constants";

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {

  tabs = [
    { name: AdminModel.AdminUser.title, active: true },
    // { name: 'Teachers', active: false },
    // { name: 'Students', active: false },
    // { name: 'Sessions', active: false },
    // { name: 'Courses', active: false }
  ];
  activeTab = this.tabs[0];

  adminPageFlex = {
    lg: '1280px',
    xs: '100%'
  };

  constructor() {
  }

  ngOnInit() {
  }

  selectTab(tab) {
    this.tabs.forEach(tab => tab.active = false);
    tab.active = true;
    this.activeTab = tab;
  }

}
