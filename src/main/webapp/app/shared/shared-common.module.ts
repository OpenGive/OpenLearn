import { NgModule, Sanitizer } from '@angular/core';
import { Title } from '@angular/platform-browser';
import {
    OpenLearnSharedLibsModule,
} from './';

@NgModule({
    imports: [
        OpenLearnSharedLibsModule
    ],
    declarations: [
    ],
    providers: [
        Title
    ],
    exports: [
        OpenLearnSharedLibsModule,
    ]
})
export class OpenLearnSharedCommonModule {}
