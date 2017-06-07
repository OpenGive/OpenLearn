import { HttpWrapperService } from '../shared/auth/http-wrapper.service';
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Achievement } from "../models/achievement.model";

@Injectable()
export class CourseService {

  private endpoint = '/api/achievements';

  constructor(private _http: HttpWrapperService) {}

  getAllAchievements(): Observable<Achievement[]> {
    return this._http.get(this.endpoint)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  getAchievement(id: number): Observable<Achievement> {
  	return this._http.get(this.endpoint + '/' + id)
  		.map(resp => resp.json())
  		.catch(this.handleError);
  }

  createAchievement(achievement: Achievement): Observable<Achievement> {
    return this._http.post(this.endpoint, achievement)
      .map(resp => resp.json())
      .catch(this.handleError);
  }

  updateAchievement(achievementToUpdate: Achievement): Observable<Achievement> {
  	return this._http.put(this.endpoint, achievementToUpdate)
  		.map(resp => resp.json())
  		.catch(this.handleError);
  }

  deleteAchievement(id: number) {
  	return this._http.delete(this.endpoint + '/' + id)
  		.map(resp => resp.json())
  		.catch(this.handleError);
  }

  private handleError(error: Response) {
    console.error(error);
    return Observable.throw(error.json() || 'Server Error');
  }
}
