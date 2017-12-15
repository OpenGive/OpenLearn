import { NgModule, Sanitizer } from '@angular/core';
import { Title } from '@angular/platform-browser';
import {
    OpenLearnSharedLibsModule,
} from './shared-libs.module';

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
