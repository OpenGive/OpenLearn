import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import * as _ from "lodash";

import {AppConstants} from "../app.constants";
import {HttpWrapperService} from '../shared/auth/http-wrapper.service';
import {ItemLinkModel} from "../models/item-link.model";


@Injectable()
export class ItemLinkService {

  private endpoint = '/api/item-links';

  constructor(private _http: HttpWrapperService) {}

  getAll(): Observable<ItemLinkModel[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  create(name: string, description: string, thumbnailImageUrl: string, itemUrl: string): Observable<ItemLinkModel>{
    var itemLink: ItemLinkModel = new ItemLinkModel(null,name, description, thumbnailImageUrl, itemUrl);

    return this._http.post(this.endpoint, itemLink)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  update(itemLink: ItemLinkModel): Observable<ItemLinkModel> {
    return this._http.put(this.endpoint, itemLink)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  delete(id: Number) {
    return this._http.delete(this.endpoint + '/' + id)
      .map(resp => resp)
      .catch(this.handleError);
  }

  get(id: Number): Observable<ItemLinkModel> {
    return this._http.get(this.endpoint + '/' + id)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  private handleError(error: Response) {
    console.error(error);
    return Observable.throw(error.json() || {message: 'Server Error'});
  }

}
