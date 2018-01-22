import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AlarmRegion } from './alarm-region.model';
import { AlarmRegionPopupService } from './alarm-region-popup.service';
import { AlarmRegionService } from './alarm-region.service';

@Component({
    selector: 'jhi-alarm-region-dialog',
    templateUrl: './alarm-region-dialog.component.html'
})
export class AlarmRegionDialogComponent implements OnInit {

    alarmRegion: AlarmRegion;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alarmRegionService: AlarmRegionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.alarmRegion.id !== undefined) {
            this.subscribeToSaveResponse(
                this.alarmRegionService.update(this.alarmRegion));
        } else {
            this.subscribeToSaveResponse(
                this.alarmRegionService.create(this.alarmRegion));
        }
    }

    private subscribeToSaveResponse(result: Observable<AlarmRegion>) {
        result.subscribe((res: AlarmRegion) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AlarmRegion) {
        this.eventManager.broadcast({ name: 'alarmRegionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-alarm-region-popup',
    template: ''
})
export class AlarmRegionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private alarmRegionPopupService: AlarmRegionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.alarmRegionPopupService
                    .open(AlarmRegionDialogComponent as Component, params['id']);
            } else {
                this.alarmRegionPopupService
                    .open(AlarmRegionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
