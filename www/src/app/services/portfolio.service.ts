import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import { ResponseContentType, RequestOptions } from '@angular/http';

import {HttpWrapperService} from '../shared/auth/http-wrapper.service';
import {User} from "../models/user.model";

@Injectable()
export class PortfolioService {

  private endpoint = '/api/portfolio-items';

  constructor(private _http: HttpWrapperService) {}

  getPortfolioByStudent(studentId: Number): Observable<any[]> {
    return this._http.get(this.endpoint + '/portfolio/' + studentId)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getPortfolioFiles(portfolioId: Number): Observable<any[]> {
    return this._http.get(this.endpoint + '/' + portfolioId + '/uploads')
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  deletePortfolioFile(portfolioId: Number, keyName: String): Observable<any[]> {
    return this._http.delete(this.endpoint + '/' + portfolioId + '/upload/' + keyName)
      .catch(this.handleError);
  }

  getPortfolioFile(portfolioId: Number, keyName: String): Observable<Blob> {
    let options = new RequestOptions({responseType: ResponseContentType.Blob });
    return this._http.getWithOptions(this.endpoint + '/' + portfolioId + '/upload/' + keyName, options)
      .map(res => res.blob())
      .catch(this.handleError);
  }

  private handleError(error: Response) {
    console.error(error);
    return Observable.throw(error.json() || {message: 'Server Error'});
  }

}
