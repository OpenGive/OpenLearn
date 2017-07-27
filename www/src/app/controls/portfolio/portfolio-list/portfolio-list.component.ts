import {Component, Input} from '@angular/core';

import {PortfolioItem} from '../../../models/portfolio-item';

@Component({
  selector: 'app-portfolio-list',
  templateUrl: './portfolio-list.component.html',
  styleUrls: ['./portfolio-list.component.css']
})
export class PortfolioListComponent {

  @Input() portfolios: PortfolioItem[] = [];
}
