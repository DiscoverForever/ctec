import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AlarmRegion } from './alarm-region.model';
import { AlarmRegionPopupService } from './alarm-region-popup.service';
import { AlarmRegionService } from './alarm-region.service';

@Component({
    selector: 'jhi-alarm-region-delete-dialog',
    templateUrl: './alarm-region-delete-dialog.component.html'
})
export class AlarmRegionDeleteDialogComponent {

    alarmRegion: AlarmRegion;

    constructor(
        private alarmRegionService: AlarmRegionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.alarmRegionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'alarmRegionListModification',
                content: 'Deleted an alarmRegion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-alarm-region-delete-popup',
    template: ''
})
export class AlarmRegionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private alarmRegionPopupService: AlarmRegionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.alarmRegionPopupService
                .open(AlarmRegionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
