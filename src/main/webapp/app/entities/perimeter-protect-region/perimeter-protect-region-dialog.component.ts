import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PerimeterProtectRegion } from './perimeter-protect-region.model';
import { PerimeterProtectRegionPopupService } from './perimeter-protect-region-popup.service';
import { PerimeterProtectRegionService } from './perimeter-protect-region.service';

@Component({
    selector: 'jhi-perimeter-protect-region-dialog',
    templateUrl: './perimeter-protect-region-dialog.component.html'
})
export class PerimeterProtectRegionDialogComponent implements OnInit {

    perimeterProtectRegion: PerimeterProtectRegion;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private perimeterProtectRegionService: PerimeterProtectRegionService,
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
        if (this.perimeterProtectRegion.id !== undefined) {
            this.subscribeToSaveResponse(
                this.perimeterProtectRegionService.update(this.perimeterProtectRegion));
        } else {
            this.subscribeToSaveResponse(
                this.perimeterProtectRegionService.create(this.perimeterProtectRegion));
        }
    }

    private subscribeToSaveResponse(result: Observable<PerimeterProtectRegion>) {
        result.subscribe((res: PerimeterProtectRegion) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: PerimeterProtectRegion) {
        this.eventManager.broadcast({ name: 'perimeterProtectRegionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-perimeter-protect-region-popup',
    template: ''
})
export class PerimeterProtectRegionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private perimeterProtectRegionPopupService: PerimeterProtectRegionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.perimeterProtectRegionPopupService
                    .open(PerimeterProtectRegionDialogComponent as Component, params['id']);
            } else {
                this.perimeterProtectRegionPopupService
                    .open(PerimeterProtectRegionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
