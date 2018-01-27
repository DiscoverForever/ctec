import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PerimeterProtectRegion } from './perimeter-protect-region.model';
import { PerimeterProtectRegionPopupService } from './perimeter-protect-region-popup.service';
import { PerimeterProtectRegionService } from './perimeter-protect-region.service';

@Component({
    selector: 'jhi-perimeter-protect-region-delete-dialog',
    templateUrl: './perimeter-protect-region-delete-dialog.component.html'
})
export class PerimeterProtectRegionDeleteDialogComponent {

    perimeterProtectRegion: PerimeterProtectRegion;

    constructor(
        private perimeterProtectRegionService: PerimeterProtectRegionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.perimeterProtectRegionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'perimeterProtectRegionListModification',
                content: 'Deleted an perimeterProtectRegion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-perimeter-protect-region-delete-popup',
    template: ''
})
export class PerimeterProtectRegionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private perimeterProtectRegionPopupService: PerimeterProtectRegionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.perimeterProtectRegionPopupService
                .open(PerimeterProtectRegionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
