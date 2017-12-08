import { SpyObject } from './spyobject';
import Spy = jasmine.Spy;
import {PrincipalService} from "../../src/app/shared/auth/principal.service";

export class MockPrincipal extends SpyObject {

    identitySpy: Spy;
    fakeResponse: any;

    constructor() {
        super(PrincipalService);

        this.fakeResponse = {};
        this.identitySpy = this.spy('identity').andReturn(Promise.resolve(this.fakeResponse));
    }

    setResponse(json: any): void {
        this.fakeResponse = json;
    }
}
