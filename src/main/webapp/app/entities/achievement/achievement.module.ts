import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OpengiveSharedModule } from '../../shared';
import { OpengiveAdminModule } from '../../admin/admin.module';

import {
    AchievementService,
    AchievementPopupService,
    AchievementComponent,
    AchievementDetailComponent,
    AchievementDialogComponent,
    AchievementPopupComponent,
    AchievementDeletePopupComponent,
    AchievementDeleteDialogComponent,
    achievementRoute,
    achievementPopupRoute,
} from './';

let ENTITY_STATES = [
    ...achievementRoute,
    ...achievementPopupRoute,
];

@NgModule({
    imports: [
        OpengiveSharedModule,
        OpengiveAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AchievementComponent,
        AchievementDetailComponent,
        AchievementDialogComponent,
        AchievementDeleteDialogComponent,
        AchievementPopupComponent,
        AchievementDeletePopupComponent,
    ],
    entryComponents: [
        AchievementComponent,
        AchievementDialogComponent,
        AchievementPopupComponent,
        AchievementDeleteDialogComponent,
        AchievementDeletePopupComponent,
    ],
    providers: [
        AchievementService,
        AchievementPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpengiveAchievementModule {}
