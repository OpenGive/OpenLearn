import {HttpWrapperService} from '../shared/auth/http-wrapper.service';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Course} from "../models/course";

@Injectable()
export class CourseService {

  private endpoint = '/api/courses';

  constructor(private _http: HttpWrapperService) {}

  getAll(): Observable<Course[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  create(course: Course): Observable<Course> {
    return this._http.post(this.endpoint, course)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  update(course: Course): Observable<Course> {
    return this._http.put(this.endpoint, course)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  delete(id: Number) {
    return this._http.delete(this.endpoint + '/' + id)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  get(id: Number): Observable<Course> {
    return this._http.get(this.endpoint + '/' + id)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  private handleError(error: Response) {
    console.error(error);
    return Observable.throw(error.json() || {message: 'Server Error'});
  }
}
