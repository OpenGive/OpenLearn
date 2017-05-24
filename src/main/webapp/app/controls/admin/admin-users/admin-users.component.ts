import {Component, OnInit} from '@angular/core';
import {GridModel} from "../../../shared/grid/grid.model";
import {ColumnModel} from "../../../shared/grid/column.model";

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
      'title',
      [
        new ColumnModel('firstname', 'First Name'),
        new ColumnModel('lastname', 'Last Name')
      ],
      [
        {
          firstname: 'John',
          lastname: 'Doe'
        },
        {
          firstname: 'Bob',
          lastname: 'Smith'
        }
      ]
    );
  }

}
