import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CtecSharedModule } from '../shared';

import { HOME_ROUTE, HomeComponent } from './';
import { NgbCarouselModule} from '@ng-bootstrap/ng-bootstrap';
@NgModule({
    imports: [
        CtecSharedModule,
        NgbCarouselModule,
        RouterModule.forChild([ HOME_ROUTE ])
    ],
    declarations: [
        HomeComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CtecHomeModule {}
