import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CtecSharedModule } from '../../shared';
import {
    PerimeterProtectRegionService,
    PerimeterProtectRegionPopupService,
    PerimeterProtectRegionComponent,
    PerimeterProtectRegionDetailComponent,
    PerimeterProtectRegionDialogComponent,
    PerimeterProtectRegionPopupComponent,
    PerimeterProtectRegionDeletePopupComponent,
    PerimeterProtectRegionDeleteDialogComponent,
    perimeterProtectRegionRoute,
    perimeterProtectRegionPopupRoute,
    PerimeterProtectRegionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...perimeterProtectRegionRoute,
    ...perimeterProtectRegionPopupRoute,
];

@NgModule({
    imports: [
        CtecSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PerimeterProtectRegionComponent,
        PerimeterProtectRegionDetailComponent,
        PerimeterProtectRegionDialogComponent,
        PerimeterProtectRegionDeleteDialogComponent,
        PerimeterProtectRegionPopupComponent,
        PerimeterProtectRegionDeletePopupComponent,
    ],
    entryComponents: [
        PerimeterProtectRegionComponent,
        PerimeterProtectRegionDialogComponent,
        PerimeterProtectRegionPopupComponent,
        PerimeterProtectRegionDeleteDialogComponent,
        PerimeterProtectRegionDeletePopupComponent,
    ],
    providers: [
        PerimeterProtectRegionService,
        PerimeterProtectRegionPopupService,
        PerimeterProtectRegionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CtecPerimeterProtectRegionModule {}
