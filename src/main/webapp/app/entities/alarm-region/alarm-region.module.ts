import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CtecSharedModule } from '../../shared';
import {
    AlarmRegionService,
    AlarmRegionPopupService,
    AlarmRegionComponent,
    AlarmRegionDetailComponent,
    AlarmRegionDialogComponent,
    AlarmRegionPopupComponent,
    AlarmRegionDeletePopupComponent,
    AlarmRegionDeleteDialogComponent,
    alarmRegionRoute,
    alarmRegionPopupRoute,
    AlarmRegionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...alarmRegionRoute,
    ...alarmRegionPopupRoute,
];

@NgModule({
    imports: [
        CtecSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AlarmRegionComponent,
        AlarmRegionDetailComponent,
        AlarmRegionDialogComponent,
        AlarmRegionDeleteDialogComponent,
        AlarmRegionPopupComponent,
        AlarmRegionDeletePopupComponent,
    ],
    entryComponents: [
        AlarmRegionComponent,
        AlarmRegionDialogComponent,
        AlarmRegionPopupComponent,
        AlarmRegionDeleteDialogComponent,
        AlarmRegionDeletePopupComponent,
    ],
    providers: [
        AlarmRegionService,
        AlarmRegionPopupService,
        AlarmRegionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CtecAlarmRegionModule {}
