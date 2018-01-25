import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CtecCameraModule } from './camera/camera.module';
import { CtecAlarmRegionModule } from './alarm-region/alarm-region.module';
import { CtecAlarmHistoryModule } from './alarm-history/alarm-history.module';
import { CtecAnalysisTaskModule } from './analysis-task/analysis-task.module';
import { CtecClusterModule } from './cluster/cluster.module';
import { CtecPerimeterProtectRegionModule } from './perimeter-protect-region/perimeter-protect-region.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CtecCameraModule,
        CtecAlarmRegionModule,
        CtecAlarmHistoryModule,
        CtecAnalysisTaskModule,
        CtecClusterModule,
        CtecPerimeterProtectRegionModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CtecEntityModule {}
