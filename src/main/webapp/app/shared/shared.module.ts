import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';

import { CookieService } from 'ngx-cookie';
import {
    OpengiveSharedLibsModule,
    OpengiveSharedCommonModule,
    CSRFService,
    AuthService,
    AuthServerProvider,
    AccountService,
    StateStorageService,
    LoginService,
    Principal,
    HasAnyAuthorityDirective,
    HasAtLeastAuthorityDirective
} from './';

@NgModule({
    imports: [
        OpengiveSharedLibsModule,
        OpengiveSharedCommonModule
    ],
    declarations: [
        HasAnyAuthorityDirective,
        HasAtLeastAuthorityDirective
    ],
    providers: [
        CookieService,
        LoginService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        AuthService,
        DatePipe
    ],
//    entryComponents: [JhiLoginModalComponent],
    exports: [
        OpengiveSharedCommonModule,
        HasAnyAuthorityDirective,
        HasAtLeastAuthorityDirective,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class OpengiveSharedModule {}
