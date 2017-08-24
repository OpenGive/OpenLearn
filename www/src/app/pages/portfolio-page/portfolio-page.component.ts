import {Component} from '@angular/core';

import {Course} from '../../models/course';

import {PortfolioItem} from "../../models/portfolio-item";
import {PortfolioService} from "../../services/portfolio.service";
import {CourseService} from "../../services/course.service";
import {Principal} from "../../shared/auth/principal.service";

@Component({
  selector: 'app-portfolio-page',
  templateUrl: './portfolio-page.component.html',
  styleUrls: ['./portfolio-page.component.css']
})
export class PortfolioPageComponent {

  constructor(private principal: Principal,
              private portfolioService: PortfolioService,
              private courseService: CourseService) {}

  portfolios: PortfolioItem[] = [];
  courses: Course[] = [];

  loadFakePortfolios() {
    for(var i = 1; i <= 10; i++){
      var item = new PortfolioItem();
      item.name = "testname" + i;
      item.filename = "testfilename" + i + ".jpg";
      item.photoUrl = "http://image.prntscr.com/image/8d081d23bd584342ba119fb562510f37.png";
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

    this.courseService.getStudentsCourses(this.principal.getId()).subscribe(courses => {
      this.courses = courses;
    })
  }
}
