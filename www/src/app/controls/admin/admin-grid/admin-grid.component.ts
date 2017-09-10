import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {AdminDialogComponent} from "../admin-dialog.component";
import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminModel} from "../admin.constants";
import {AdminService} from "../../../services/admin.service";
import {AdminGridService} from "../../../services/admin-grid.service";
import {DataService} from "../../../services/data.service";

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
  programs: any[];
  sessions: any[];
  instructors: any[];

  constructor(private dialog: MdDialog,
              private adminGridService: AdminGridService,
              private adminService: AdminService,
              private dataService: DataService) {
  }

  ngOnInit(): void {
    this.getRows();
  }

  private getRows(): void {
    this.adminGridService.query(this.grid.route)
      .subscribe(resp => {
        this.grid.rows = resp;
        this.getAndMapEntities();
        this.sort(_.find(this.grid.columns, {'property': this.grid.defaultSort}), false);
      });
  }

  // Only make the calls to the backend that you need for the tab you are on
  private getAndMapEntities(): void {
    if ([AdminModel.OrgAdministrator.route, AdminModel.Instructor.route, AdminModel.Student.route,
        AdminModel.Program.route].includes(this.grid.route)) {
      this.adminService.getAll(AdminModel.Organization.route).subscribe(resp => {
        this.organizations = resp;
        this.grid.rows.forEach(row => {
          let organization = _.find(this.organizations, ['id', row.organizationId]);
          row.organization = organization === undefined ? '' : organization;
        });
      });
    }
    if (AdminModel.Session.route === this.grid.route) {
      this.adminService.getAll(AdminModel.Program.route).subscribe(resp => {
        this.programs = resp;
        this.grid.rows.forEach(row => {
          let program = _.find(this.programs, ['id', row.programId]);
          row.program = program === undefined ? '' : program;
        });
      });
    }
    if (AdminModel.Course.route === this.grid.route) {
      this.adminService.getAll(AdminModel.Session.route).subscribe(resp => {
        this.sessions = resp;
        this.grid.rows.forEach(row => {
          let session = _.find(this.sessions, ['id', row.sessionId]);
          row.session = session === undefined ? '' : session;
        });
      });
    }
    if (AdminModel.Course.route === this.grid.route) {
      this.adminService.getAll(AdminModel.Instructor.route).subscribe(resp => {
        this.instructors = resp;
        this.grid.rows.forEach(row => {
          let instructor = _.find(this.instructors, ['id', row.instructorId]);
          row.instructor = instructor === undefined ? '' : instructor;
        });
      });
    }
  }

  add(): void {
    this.dialog.open(AdminDialogComponent, {
      data: {
        tab: this.grid.route,
        item: {},
        organizations: this.organizations,
        adding: true
      },
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleDialogResponse(resp)
    });
  }

  viewDetails(row): void {
    switch (this.grid.route) {
      case AdminModel.Course.route:
        this.dataService.setCourseById(row.id);
        break;
      case AdminModel.Student.route:
        this.dataService.setStudentById(row.id);
        break;
      default:
        this.dialog.open(AdminDialogComponent, {
          data: {
            tab: this.grid.route,
            item: row,
            organizations: this.organizations,
            adding: false
          },
          disableClose: true
        }).afterClosed().subscribe(resp => {
          this.handleDialogResponse(resp)
        });
        break;
    }
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
    if (['endDate', 'startDate'].includes(column.property)) {
      return this.displayDate(row[column.property]);
    // } else if (['organizationId', 'programId', 'sessionId'].includes(column.property)) {
    //   return this.displayObject(row[column.property]);
    // } else if (['instructorId'].includes(column.property)) {
    //   return this.displayUser(row[column.property]);
    } else {
      return row[column.property];
    }
  }

  private displayDate(date): string {
    return date ? new Date(date).toLocaleDateString() : '';
  }

  // private displayObject(object): string {
  //   return _.isNil(object) ? '' : object.name;
  // }
  //
  // private displayUser(user): string {
  //   return user.lastName + ', ' + user.firstName;
  // }

  sort(column: any, reverse ?: boolean): void {
    if (!_.isNil(reverse)) { // use reverse parameter if available
      this.reverse = reverse;
    } else {
      this.reverse = (this.sortColumn === column.property ? !this.reverse : false);
    }
    this.sortColumn = column.property;
    // if (['organizationId', 'programId', 'sessionId'].includes(column.property)) {
    //   this.filteredRows = _.sortBy(this.grid.rows, [row => this.displayObject(row[column.property])]);
    // } else if (['instructorId'].includes(column.property)) {
    //   this.filteredRows = _.sortBy(this.grid.rows, [row => this.displayUser(row[column.property])]);
    // } else {
    this.filteredRows = _.sortBy(this.grid.rows, [row => row[column.property]]);
    // }
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
