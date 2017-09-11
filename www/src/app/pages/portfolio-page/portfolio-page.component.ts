import {Component} from '@angular/core';

import {Course} from '../../models/course.model';

import {PortfolioItem} from "../../models/portfolio-item.model";
import {PortfolioService} from "../../services/portfolio.service";
import {StudentCourseService} from "../../services/student-course.service";
import {Principal} from "../../shared/auth/principal.service";

@Component({
  selector: 'app-portfolio-page',
  templateUrl: './portfolio-page.component.html',
  styleUrls: ['./portfolio-page.component.css']
})
export class PortfolioPageComponent {

  constructor(private principal: Principal,
              private portfolioService: PortfolioService,
              private courseService: StudentCourseService) {}

  portfolios: PortfolioItem[] = [];
  courses: Course[] = [];

  loadFakePortfolios() {
    for(var i = 1; i <= 10; i++){
      var item = new PortfolioItem();
      item.name = "testname" + i;
      this.portfolios.push(item);
    }
  }

  ngOnInit() {
    this.portfolioService.getAllPortfolios().subscribe(portfolios => {
      //this.portfolios = portfolios;
      this.loadFakePortfolios();
    });

    this.getCourses();
  }

  private getCourses(): void {

    this.courseService.getStudentCoursesByStudent(this.principal.getId()).subscribe(courses => {
      this.courses = courses;
    })
  }
}
