import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OpengiveSharedModule } from '../../shared';

import {
    ItemLinkService,
    ItemLinkPopupService,
    ItemLinkComponent,
    ItemLinkDetailComponent,
    ItemLinkDialogComponent,
    ItemLinkPopupComponent,
    ItemLinkDeletePopupComponent,
    ItemLinkDeleteDialogComponent,
    itemLinkRoute,
    itemLinkPopupRoute,
} from './';

let ENTITY_STATES = [
    ...itemLinkRoute,
    ...itemLinkPopupRoute,
];

@NgModule({
    imports: [
        OpengiveSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ItemLinkComponent,
        ItemLinkDetailComponent,
        ItemLinkDialogComponent,
        ItemLinkDeleteDialogComponent,
        ItemLinkPopupComponent,
        ItemLinkDeletePopupComponent,
    ],
    entryComponents: [
        ItemLinkComponent,
        ItemLinkDialogComponent,
        ItemLinkPopupComponent,
        ItemLinkDeleteDialogComponent,
        ItemLinkDeletePopupComponent,
    ],
    providers: [
        ItemLinkService,
        ItemLinkPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpengiveItemLinkModule {}
