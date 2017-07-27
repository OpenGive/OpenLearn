import {Component, Input} from '@angular/core';

import {PortfolioItem} from '../../../models/portfolio-item'

@Component({
  selector: 'app-portfolio-card',
  templateUrl: './portfolio-card.component.html',
  styleUrls: ['./portfolio-card.component.css']
})
export class PortfolioCardComponent {

  @Input() portfolioItem: PortfolioItem;

  share() {
    alert(this.portfolioItem.filename);
  }

}
