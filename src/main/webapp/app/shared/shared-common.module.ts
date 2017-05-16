import { NgModule, Sanitizer } from '@angular/core';
import { Title } from '@angular/platform-browser';
import {
    OpengiveSharedLibsModule,
} from './';

@NgModule({
    imports: [
        OpengiveSharedLibsModule
    ],
    declarations: [
    ],
    providers: [
        Title
    ],
    exports: [
        OpengiveSharedLibsModule,
    ]
})
export class OpengiveSharedCommonModule {}
