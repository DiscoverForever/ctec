import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CtecSharedModule } from '../../shared';
import {
    ClusterService,
    ClusterPopupService,
    ClusterComponent,
    ClusterDetailComponent,
    ClusterDialogComponent,
    ClusterPopupComponent,
    ClusterDeletePopupComponent,
    ClusterDeleteDialogComponent,
    clusterRoute,
    clusterPopupRoute,
    ClusterResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...clusterRoute,
    ...clusterPopupRoute,
];

@NgModule({
    imports: [
        CtecSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ClusterComponent,
        ClusterDetailComponent,
        ClusterDialogComponent,
        ClusterDeleteDialogComponent,
        ClusterPopupComponent,
        ClusterDeletePopupComponent,
    ],
    entryComponents: [
        ClusterComponent,
        ClusterDialogComponent,
        ClusterPopupComponent,
        ClusterDeleteDialogComponent,
        ClusterDeletePopupComponent,
    ],
    providers: [
        ClusterService,
        ClusterPopupService,
        ClusterResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CtecClusterModule {}
