import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Camera } from './camera.model';
import { CameraPopupService } from './camera-popup.service';
import { CameraService } from './camera.service';
import { AlarmRegion, AlarmRegionService } from '../alarm-region';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-camera-dialog',
    templateUrl: './camera-dialog.component.html'
})
export class CameraDialogComponent implements OnInit {

    camera: Camera;
    isSaving: boolean;

    alarmregions: AlarmRegion[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cameraService: CameraService,
        private alarmRegionService: AlarmRegionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.alarmRegionService
            .query({filter: 'camera-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.camera.alarmRegion || !this.camera.alarmRegion.id) {
                    this.alarmregions = res.json;
                } else {
                    this.alarmRegionService
                        .find(this.camera.alarmRegion.id)
                        .subscribe((subRes: AlarmRegion) => {
                            this.alarmregions = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
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

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackAlarmRegionById(index: number, item: AlarmRegion) {
        return item.id;
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
