import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { AlarmRegion } from './alarm-region.model';
import { AlarmRegionService } from './alarm-region.service';

@Component({
    selector: 'jhi-alarm-region-detail',
    templateUrl: './alarm-region-detail.component.html'
})
export class AlarmRegionDetailComponent implements OnInit, OnDestroy {

    alarmRegion: AlarmRegion;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private alarmRegionService: AlarmRegionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAlarmRegions();
    }

    load(id) {
        this.alarmRegionService.find(id).subscribe((alarmRegion) => {
            this.alarmRegion = alarmRegion;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAlarmRegions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'alarmRegionListModification',
            (response) => this.load(this.alarmRegion.id)
        );
    }
}
