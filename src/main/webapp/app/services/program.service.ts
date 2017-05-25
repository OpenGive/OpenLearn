import { Program } from './../models/program';
import { HttpWrapperService } from './../shared/auth/http-wrapper.service';
import { Injectable } from "@angular/core";
import { Http, Headers, RequestOptions} from "@angular/http";
import { Observable } from "rxjs";
import { log } from "util";

import 'rxjs/Rx';

@Injectable()
export class ProgramService {
	constructor(private _http: HttpWrapperService) {
	}

	createProgram(programToCreate: Program) {
		return this._http.post('/api/createProgram', programToCreate)
		.map(response => response.json())
		.catch(this.handleError);
	}

	getAllPrograms(): Observable<Program[]> {
		return this._http.get('/api/programs')
		.map(response => <Program[]> response.json())
		.catch(this.handleError);
	}

	updateProgram(programToUpdate: Program): Observable<Program> {
		return this._http.put('/api/programs', programToUpdate)
		.map(response => <Program> response.json())
		.catch(this.handleError);
	}

	deleteProgram(id: number) {
		return this._http.delete('/api/programs/' + id)
		.map(response => response.json())
		.catch(this.handleError);
	}


	getProgram(id: number): Observable<Program> {
		return this._http.get('/api/programs/' + id)
		.map(response => <Program> response.json())
		.catch(this.handleError)
	}

	private handleError(error: Response) {
		console.error(error);
		return Observable.throw(error.json() || 'Server Error');
	}


}