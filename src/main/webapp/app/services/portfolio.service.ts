import { HttpWrapperService } from './../shared/auth/http-wrapper.service';
import { PortfolioItem } from '../models/portfolio-item';
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class PortfolioService {

  constructor(private httpWrapper: HttpWrapperService) {}

  getAllPortfolios() {
    return this.httpWrapper.get('/api/portfolios').map(resp => {
      let json = resp.json();
      return json;
    });
  }

  getAllPortfolioItems() : Observable<PortfolioItem[]> {
    return this.httpWrapper.get('/api/portfolio-items').map(resp => {
      let json = resp.json();
      return json;
    });
  }
}
