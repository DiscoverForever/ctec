import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CtecSharedModule } from '../../shared';
import {
    CameraService,
    CameraPopupService,
    CameraComponent,
    CameraDetailComponent,
    CameraDialogComponent,
    CameraPopupComponent,
    CameraDeletePopupComponent,
    CameraDeleteDialogComponent,
    cameraRoute,
    cameraPopupRoute,
    CameraResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cameraRoute,
    ...cameraPopupRoute,
];

@NgModule({
    imports: [
        CtecSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CameraComponent,
        CameraDetailComponent,
        CameraDialogComponent,
        CameraDeleteDialogComponent,
        CameraPopupComponent,
        CameraDeletePopupComponent,
    ],
    entryComponents: [
        CameraComponent,
        CameraDialogComponent,
        CameraPopupComponent,
        CameraDeleteDialogComponent,
        CameraDeletePopupComponent,
    ],
    providers: [
        CameraService,
        CameraPopupService,
        CameraResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CtecCameraModule {}
