import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {AdminDialogComponent} from "../admin-dialog.component";
import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminModel} from "../admin.constants";
import {AdminService} from "../../../services/admin.service";
import {AdminGridService} from "../../../services/admin-grid.service";
import {forEach} from "@angular/router/src/utils/collection";

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
  organizations: any[];

  constructor(private dialog: MdDialog,
              private adminGridService: AdminGridService,
              private adminService: AdminService) {}

  ngOnInit(): void {
    this.getRows();
    this.getOrganizations();
  }

  private getRows(): void {
    this.adminGridService.query(this.grid.route)
      .subscribe(resp => {
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
      } else if (resp.type === 'DELETE') {
        _.remove(this.grid.rows, row => row.id === resp.data.id);
      }
      this.sort(_.find(this.grid.columns, {'property': this.sortColumn}), this.reverse);
    }
  }

  displayCell(row, column): string {
    if (['authorities'].includes(column.property)) {
      return this.displayAuthorities(row[column.property]);
    } else if (['endDate', 'startDate'].includes(column.property)) {
      return this.displayDate(row[column.property]);
    } else if (['organizations', 'organizationIds'].includes(column.property)) {
      return this.displayOrganization(row[column.property]);
    } else if (['course', 'milestone', 'program', 'school', 'session'].includes(column.property)) {
      return this.displayObject(row[column.property]);
    } else if (['achievedBy', 'instructor'].includes(column.property)) {
      return this.displayUser(row[column.property]);
    } else {
      return row[column.property];
    }
  }

  private displayAuthorities(authorities): string { // Convert "ROLE_ONE_TWO" to "One Two"
    return authorities.map(role => {
      return role.split('_').slice(1).map(str => str.charAt(0) + str.slice(1).toLowerCase()).join(' ');
    }).sort().join(', ');
  }

  private displayOrganization(organization): string {
    if(_.isNil(this.organizations) || _.isNil(organization) || organization[0] < 0 ){
      return '';
    } else{
      let id = parseFloat(organization[0]);
      for (var i = 0; i < this.organizations.length; i++) {
        if(this.organizations[i].id === id){
          return this.organizations[i].name;
        }
      }
      return '';
    }
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

  private getOrganizations(): void {
    this.adminService.getAll(AdminModel.Organization.route).subscribe(resp => {this.organizations = resp;console.log("Trying to print orgs: " + JSON.stringify(this.organizations));});

  }
}
