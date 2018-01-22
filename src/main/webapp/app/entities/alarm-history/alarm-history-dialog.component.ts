import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { AlarmHistory } from './alarm-history.model';
import { AlarmHistoryPopupService } from './alarm-history-popup.service';
import { AlarmHistoryService } from './alarm-history.service';
import { Camera, CameraService } from '../camera';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-alarm-history-dialog',
    templateUrl: './alarm-history-dialog.component.html'
})
export class AlarmHistoryDialogComponent implements OnInit {

    alarmHistory: AlarmHistory;
    isSaving: boolean;

    cameras: Camera[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private alarmHistoryService: AlarmHistoryService,
        private cameraService: CameraService,
        private elementRef: ElementRef,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.cameraService
            .query({filter: 'alarmhistory-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.alarmHistory.camera || !this.alarmHistory.camera.id) {
                    this.cameras = res.json;
                } else {
                    this.cameraService
                        .find(this.alarmHistory.camera.id)
                        .subscribe((subRes: Camera) => {
                            this.cameras = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.alarmHistory, this.elementRef, field, fieldContentType, idInput);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.alarmHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.alarmHistoryService.update(this.alarmHistory));
        } else {
            this.subscribeToSaveResponse(
                this.alarmHistoryService.create(this.alarmHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<AlarmHistory>) {
        result.subscribe((res: AlarmHistory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AlarmHistory) {
        this.eventManager.broadcast({ name: 'alarmHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCameraById(index: number, item: Camera) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-alarm-history-popup',
    template: ''
})
export class AlarmHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private alarmHistoryPopupService: AlarmHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.alarmHistoryPopupService
                    .open(AlarmHistoryDialogComponent as Component, params['id']);
            } else {
                this.alarmHistoryPopupService
                    .open(AlarmHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
