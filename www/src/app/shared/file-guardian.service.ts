import {Injectable} from '@angular/core';
import {PortfolioItem} from '../models/portfolio-item.model';
import {Assignment} from '../models/assignment.model';

@Injectable()
export class FileGuardian {
  constructor() {}

  canHaveFiles(instance: any): boolean {
    return (this.isPortfolio(instance) ||
      this.isAssignment(instance)) &&
      instance.hasOwnProperty('id') &&
      instance.id != undefined &&
      instance.id != null
  }

  isPortfolio(instance: any): instance is PortfolioItem {
    return instance && (<PortfolioItem> instance).url !== undefined;
  }

  isAssignment(instance: any): instance is Assignment {
    return instance && (<Assignment> instance).courseId !== undefined;
  }

}
