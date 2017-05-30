import { Component, OnInit } from '@angular/core';
import { PortfolioService } from '../../../services/portfolio.service'
import { PortfolioCardComponent } from '../portfolio-card/portfolio-card.component'

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements OnInit {

  constructor(private portfolioService: PortfolioService) {}

  portfolios: PortfolioCardComponent[] = [];

  ngOnInit() {
    this.portfolioService.getAllPortfolios().subscribe(portfolios => {
      this.portfolios = portfolios;
    });
  }

}
