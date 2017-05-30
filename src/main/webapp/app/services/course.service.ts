import { Course } from './../models/course';
import { HttpWrapperService } from './../shared/auth/http-wrapper.service';
import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs";
import {log} from "util";

@Injectable()
export class CourseService {
	constructor(private _http: HttpWrapperService) {}

	getAllCourses(): Observable<Course[]> {
		return this._http.get('/api/courses').map(resp => {
			let json = resp.json();
			return json as Course[];
		});
	}

	createCourse(courseToCreate: Course) {
		return this._http.post('/api/courses', courseToCreate)
		.map(response => <Course> response.json())
		.catch(this.handleError);
	}

	updateCourse(courseToUpdate: Course): Observable<Course> {
		return this._http.put('/api/courses', courseToUpdate)
		.map(response => <Course> response.json())
		.catch(this.handleError);
	}

	deleteCourse(id: number) {
		return this._http.delete('/api/courses/' + id)
		.map(response => response.json())
		.catch(this.handleError);
	}

	getCourse(id: number): Observable<Course> {
		return this._http.get('/api/courses/' + id)
		.map(response => <Course> response.json())
		.catch(this.handleError)
	}

	private handleError(error: Response) {
		console.error(error);
		return Observable.throw(error.json() || 'Server Error');
	}

}
