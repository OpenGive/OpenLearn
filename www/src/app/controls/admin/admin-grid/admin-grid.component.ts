import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {AdminDialogComponent} from "../admin-dialog.component";
import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminGridService} from "../../../services/admin-grid.service";

@Component({
  selector: 'app-admin-grid',
  templateUrl: './admin-grid.component.html',
  styleUrls: ['./admin-grid.component.css']
})
export class AdminGridComponent implements OnInit {

  @Input() grid: AdminGridModel;
  filteredRows: any[];

  sortColumn: any;
  reverse: boolean;

  constructor(private dialog: MdDialog,
              private adminGridService: AdminGridService) {}

  ngOnInit(): void {
    this.getRows();
    console.log("Is this called?");
  }

  private getRows(): void {
    this.adminGridService.query(this.grid.route)
      .subscribe(resp => {
        console.log("GET ROWS RESPONSE: " + JSON.stringify(resp));
        this.grid.rows = resp;
        this.sort(_.find(this.grid.columns, {'property': this.grid.defaultSort}), false);
      });
  }

  add(): void {
    this.dialog.open(AdminDialogComponent, {
      data: {
        tab: this.grid.route,
        item: {},
        adding: true
      },
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleDialogResponse(resp)
    });
  }

  viewDetails(row): void {
    this.dialog.open(AdminDialogComponent, {
      data: {
        tab: this.grid.route,
        item: row,
        adding: false
      },
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleDialogResponse(resp)
    });
  }

  private handleDialogResponse(resp): void {
    if (resp) {
      if (resp.type === 'UPDATE') {
        let ndx = _.findIndex(this.grid.rows, {id: resp.data.id});
        this.grid.rows[ndx] = resp.data;
      } else if (resp.type === 'ADD') {
        this.grid.rows.push(resp.data);
        console.log("HANDLE DIALOG RESPONSE: " + JSON.stringify(resp));
      } else if (resp.type === 'DELETE') {
        _.remove(this.grid.rows, row => row.id === resp.data.id);
      }
      this.sort(_.find(this.grid.columns, {'property': this.sortColumn}), this.reverse);
      // Retrieve users again from the DB so the grid doesn't break (change later)
      this.getRows();
    }
  }

  displayCell(row, column): string {
    if (['authorities'].includes(column.property)) {
      return this.displayAuthorities(row[column.property]);
    } else if (['endDate', 'startDate'].includes(column.property)) {
      return this.displayDate(row[column.property]);
    } else if (['course', 'milestone', 'organization', 'program', 'school', 'session'].includes(column.property)) {
      return this.displayObject(row[column.property]);
    } else if (['achievedBy', 'instructor'].includes(column.property)) {
      return this.displayUser(row[column.property]);
    } else {
      return row[column.property];
    }
  }

  private displayAuthorities(authorities): string { // Convert "ROLE_ONE_TWO" to "One Two"
    if (authorities.toString() == "[object Object]") {
      console.log("OBJECT = " + JSON.stringify(authorities));
    } else {
      console.log("STRING = " + authorities);
    }
    return authorities.map(role => {
      return role.split('_').slice(1).map(str => str.charAt(0) + str.slice(1).toLowerCase()).join(' ');
    }).sort().join(', ');
  }

  private displayDate(date): string {
    return date ? new Date(date).toLocaleDateString() : '';
  }

  private displayObject(object): string {
    return _.isNil(object) ? '' : object.name;
  }

  private displayUser(user): string {
    return user.lastName + ', ' + user.firstName;
  }

  sort(column: any, reverse?: boolean): void {
    if (!_.isNil(reverse)) { // use reverse parameter if available
      this.reverse = reverse;
    } else {
      this.reverse = (this.sortColumn === column.property ? !this.reverse : false);
    }
    this.sortColumn = column.property;
    if (['authorities'].includes(column.property)) {
      this.filteredRows = _.sortBy(this.grid.rows, [row => this.displayAuthorities(row[column.property])]);
    } else if (['course', 'milestone', 'organization', 'program', 'school', 'session'].includes(column.property)) {
      this.filteredRows = _.sortBy(this.grid.rows, [row => this.displayObject(row[column.property])]);
    } else if (['achievedBy', 'instructor'].includes(column.property)) {
      this.filteredRows = _.sortBy(this.grid.rows, [row => this.displayUser(row[column.property])]);
    } else {
      this.filteredRows = _.sortBy(this.grid.rows, [row => row[column.property]]);
    }
    if (this.reverse) {
      this.filteredRows.reverse();
    }
    this.updateSortIcon();
  }

  private updateSortIcon(): void {
    _.forEach(this.grid.columns, column => {
      column.sortIcon = 'sort';
    });
    let ndx = _.findIndex(this.grid.columns, {'property': this.sortColumn});
    this.grid.columns[ndx].sortIcon = (this.reverse ? 'keyboard_arrow_up' : 'keyboard_arrow_down');
  }
}
