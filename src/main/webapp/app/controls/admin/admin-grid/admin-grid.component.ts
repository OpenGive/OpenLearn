import {Component, Input, OnInit} from "@angular/core";
import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminDialogComponent} from "../admin-dialog/admin-dialog.component";
import {MdDialog} from "@angular/material";
import {AdminGridService} from "../../../services/admin-grid.service";
import * as _ from "lodash";

@Component({
  selector: 'app-admin-grid',
  templateUrl: './admin-grid.component.html',
  styleUrls: ['./admin-grid.component.css']
})
export class AdminGridComponent implements OnInit {

  @Input() grid: AdminGridModel;

  constructor(private dialog: MdDialog,
              private adminGridService: AdminGridService) {}

  ngOnInit(): void {
    this.getRows();
  }

  getRows(): void {
    this.adminGridService.query(this.grid.route).subscribe(resp => this.grid.rows = resp);
  }

  add(): void {
    this.dialog.open(AdminDialogComponent, {
      data: {
        title: this.grid.title.slice(0, -1),
        tab: this.grid.route,
        item: {},
        adding: true
      },
      disableClose: true
    }).afterClosed().subscribe(resp => this.handleDialogResponse(resp));
  }

  viewDetails(row): void {
    this.dialog.open(AdminDialogComponent, {
      data: {
        title: this.grid.title.slice(0, -1),
        tab: this.grid.route,
        item: row,
        adding: false
      },
      disableClose: true
    }).afterClosed().subscribe(resp => this.handleDialogResponse(resp));
  }

  handleDialogResponse(resp): void {
    if (resp) {
      if (resp.type === 'update') {
        let ndx = _.findIndex(this.grid.rows, {id: resp.data.id});
        _.each(resp.data, (value, key) => this.grid.rows[ndx][key] = value);
      } else if (resp.type === 'add') {
        this.grid.rows.push(resp.data);
      } else if (resp.type === 'delete') {
        _.remove(this.grid.rows, row => row.id === resp.data);
      }
    }
  }

  displayCell(row, column): string {
    if (column.property === 'authorities') {
      return this.displayAuthorities(row[column.property]);
    } else if (column.property === 'startDate' || column.property === 'endDate') {
      return this.displayDate(row[column.property]);
    } else if (column.property === 'organization' || column.property === 'program' || column.property === 'school' || column.property === 'session') {
      return this.displayObject(row[column.property]);
    } else if (column.property === 'instructor') {
      return this.displayUser(row[column.property]);
    } else {
      return row[column.property];
    }
  }

  displayAuthorities(authorities): string { // Parse array of roles to create friendly list (ROLE_BLAH => Blah)
    return authorities.map(role => {
      return role.charAt(5) + role.slice(6).toLowerCase()
    }).sort().join(', ');
  }

  displayDate(date): string {
    return date ? new Date(date).toLocaleDateString() : '';
  }

  displayObject(object): string {
    return object.name;
  }

  displayUser(user): string {
    return user.lastName + ', ' + user.firstName;
  }
}
