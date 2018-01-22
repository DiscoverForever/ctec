import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CtecSharedModule } from '../../shared';
import {
    AnalysisTaskService,
    AnalysisTaskPopupService,
    AnalysisTaskComponent,
    AnalysisTaskDetailComponent,
    AnalysisTaskDialogComponent,
    AnalysisTaskPopupComponent,
    AnalysisTaskDeletePopupComponent,
    AnalysisTaskDeleteDialogComponent,
    analysisTaskRoute,
    analysisTaskPopupRoute,
    AnalysisTaskResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...analysisTaskRoute,
    ...analysisTaskPopupRoute,
];

@NgModule({
    imports: [
        CtecSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AnalysisTaskComponent,
        AnalysisTaskDetailComponent,
        AnalysisTaskDialogComponent,
        AnalysisTaskDeleteDialogComponent,
        AnalysisTaskPopupComponent,
        AnalysisTaskDeletePopupComponent,
    ],
    entryComponents: [
        AnalysisTaskComponent,
        AnalysisTaskDialogComponent,
        AnalysisTaskPopupComponent,
        AnalysisTaskDeleteDialogComponent,
        AnalysisTaskDeletePopupComponent,
    ],
    providers: [
        AnalysisTaskService,
        AnalysisTaskPopupService,
        AnalysisTaskResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CtecAnalysisTaskModule {}
