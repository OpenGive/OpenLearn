import {Component, OnInit} from '@angular/core';
import {GridModel} from "../../../models/grid.model";
import {AdminModel} from "../admin.constants";

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {

  userData: GridModel;

  constructor() {
  }

  ngOnInit() {
    this.userData = new GridModel(
      'Users',
      AdminModel.User.columns,
      AdminModel.User.details,
      [
        {
          login: 'stephen.hathaway',
          firstname: 'Stephen',
          lastname: 'Hathaway',
          email: 'stephen.hathaway@safetymail.info',
          phoneNumber: '972-867-5309',
          address: '1600 Pennsylvania Ave NW, Washington, DC 20006',
          is14Plus: true
        }
      ]
    );
  }

}
