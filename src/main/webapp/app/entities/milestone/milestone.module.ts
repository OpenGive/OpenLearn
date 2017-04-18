import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OpengiveSharedModule } from '../../shared';
import {
    MilestoneService,
    MilestonePopupService,
    MilestoneComponent,
    MilestoneDetailComponent,
    MilestoneDialogComponent,
    MilestonePopupComponent,
    MilestoneDeletePopupComponent,
    MilestoneDeleteDialogComponent,
    milestoneRoute,
    milestonePopupRoute,
} from './';

const ENTITY_STATES = [
    ...milestoneRoute,
    ...milestonePopupRoute,
];

@NgModule({
    imports: [
        OpengiveSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MilestoneComponent,
        MilestoneDetailComponent,
        MilestoneDialogComponent,
        MilestoneDeleteDialogComponent,
        MilestonePopupComponent,
        MilestoneDeletePopupComponent,
    ],
    entryComponents: [
        MilestoneComponent,
        MilestoneDialogComponent,
        MilestonePopupComponent,
        MilestoneDeleteDialogComponent,
        MilestoneDeletePopupComponent,
    ],
    providers: [
        MilestoneService,
        MilestonePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpengiveMilestoneModule {}
