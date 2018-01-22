import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AnalysisTask } from './analysis-task.model';
import { AnalysisTaskPopupService } from './analysis-task-popup.service';
import { AnalysisTaskService } from './analysis-task.service';

@Component({
    selector: 'jhi-analysis-task-dialog',
    templateUrl: './analysis-task-dialog.component.html'
})
export class AnalysisTaskDialogComponent implements OnInit {

    analysisTask: AnalysisTask;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private analysisTaskService: AnalysisTaskService,
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
        if (this.analysisTask.id !== undefined) {
            this.subscribeToSaveResponse(
                this.analysisTaskService.update(this.analysisTask));
        } else {
            this.subscribeToSaveResponse(
                this.analysisTaskService.create(this.analysisTask));
        }
    }

    private subscribeToSaveResponse(result: Observable<AnalysisTask>) {
        result.subscribe((res: AnalysisTask) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AnalysisTask) {
        this.eventManager.broadcast({ name: 'analysisTaskListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-analysis-task-popup',
    template: ''
})
export class AnalysisTaskPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private analysisTaskPopupService: AnalysisTaskPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.analysisTaskPopupService
                    .open(AnalysisTaskDialogComponent as Component, params['id']);
            } else {
                this.analysisTaskPopupService
                    .open(AnalysisTaskDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
