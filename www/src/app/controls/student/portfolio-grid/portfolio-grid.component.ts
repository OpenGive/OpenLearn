import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {Student} from "../../../models/student.model";
import {PortfolioFormComponent} from "../../course/portfolio/portfolio-form.component";
import {AppConstants} from "../../../app.constants";
import {Principal} from "../../../shared/auth/principal-storage.service";
import {PortfolioService} from "../../../services/portfolio.service";
import {AdminService} from "../../../services/admin.service";
import {AdminTabs} from "../../admin/admin.constants";
@Component({
  selector: 'app-portfolio-grid',
  templateUrl: './portfolio-grid.component.html',
  styleUrls: ['./portfolio-grid.component.css']
})
export class PortfolioGridComponent implements OnInit {

  @Input() student: Student;
  portfolioItems: any[];
  columns: any[];

  studentView: boolean;

  sortColumn: any;
  reverse: boolean;

  constructor(private dialog: MdDialog,
              private principal: Principal,
              private portfolioService: PortfolioService,
              private adminService: AdminService) {}

  ngOnInit(): void {

    this.studentView = this.principal.hasAuthority(AppConstants.Role.Student.name);
    this.columns = [
      {
        id: "portfolioItem.name",
        name: "Name"
      },
      {
        id: "portfolioItem.description",
        name: "Description"
      }
    ];

    this.getPortfolio();

  }

  add(): void {
    this.dialog.open(PortfolioFormComponent, {
      data: {
        student: this.student,
        portfolioItem: {},
        studentView: this.studentView,
        adding: true
      },
      width: "400px",
      height: "600px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleAddPortfolioResponse(resp)
    });
  }

  // removeStudent(id: Number): void {
  //   this.courseService.deleteStudentCourse(id).subscribe(resp => {
  //     this.students = _.filter(this.students, student => student.id !== id);
  //   });
  // }
  removePortfolioItem(id: Number): void {
    this.adminService.delete(AdminTabs.Portfolio.route,id).subscribe(resp => {
      this.portfolioItems = _.filter(this.portfolioItems, item => item.id !== id);
    })
  }

  getPortfolio(): void {
    this.portfolioService.getPortfolioByStudent(this.student.id).subscribe(resp => {
      this.portfolioItems = resp;
      this.portfolioItems = _.filter(this.portfolioItems, item => item.id > -1);
      console.log(this.portfolioItems);
    })
  }

  viewPortfolioDetails(row){
    this.dialog.open(PortfolioFormComponent, {
      data: {
        student: this.student,
        portfolioItem: row,
        studentView: this.studentView,
        adding: false
      }
    }).afterClosed().subscribe(resp => {
      this.handleEditPortfolioResponse(resp)
    });
  }

  stopPropagation(e): void {
    e.stopPropagation();
  }


  private handleAddPortfolioResponse(resp): void {
    if (resp) {
      console.log("Response from add portfolio item", resp);
      this.ngOnInit();
    }
  }

  private handleEditPortfolioResponse(resp): void {
    if (resp) {
      console.log("Response from edit portfolio item", resp);
      this.ngOnInit();
    }
  }

  private handleEditGradeResponse(resp): void {
    console.log(resp);
  }
}
