import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Camera } from './camera.model';
import { CameraPopupService } from './camera-popup.service';
import { CameraService } from './camera.service';

@Component({
    selector: 'jhi-camera-dialog',
    templateUrl: './camera-dialog.component.html'
})
export class CameraDialogComponent implements OnInit {

    camera: Camera;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private cameraService: CameraService,
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
        if (this.camera.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cameraService.update(this.camera));
        } else {
            this.subscribeToSaveResponse(
                this.cameraService.create(this.camera));
        }
    }

    private subscribeToSaveResponse(result: Observable<Camera>) {
        result.subscribe((res: Camera) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Camera) {
        this.eventManager.broadcast({ name: 'cameraListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-camera-popup',
    template: ''
})
export class CameraPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cameraPopupService: CameraPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cameraPopupService
                    .open(CameraDialogComponent as Component, params['id']);
            } else {
                this.cameraPopupService
                    .open(CameraDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
