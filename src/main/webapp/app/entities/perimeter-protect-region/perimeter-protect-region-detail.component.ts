import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { PerimeterProtectRegion } from './perimeter-protect-region.model';
import { PerimeterProtectRegionService } from './perimeter-protect-region.service';

@Component({
    selector: 'jhi-perimeter-protect-region-detail',
    templateUrl: './perimeter-protect-region-detail.component.html'
})
export class PerimeterProtectRegionDetailComponent implements OnInit, OnDestroy {

    perimeterProtectRegion: PerimeterProtectRegion;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private perimeterProtectRegionService: PerimeterProtectRegionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPerimeterProtectRegions();
    }

    load(id) {
        this.perimeterProtectRegionService.find(id).subscribe((perimeterProtectRegion) => {
            this.perimeterProtectRegion = perimeterProtectRegion;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPerimeterProtectRegions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'perimeterProtectRegionListModification',
            (response) => this.load(this.perimeterProtectRegion.id)
        );
    }
}
