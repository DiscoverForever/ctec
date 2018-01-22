import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AnalysisTask } from './analysis-task.model';
import { AnalysisTaskPopupService } from './analysis-task-popup.service';
import { AnalysisTaskService } from './analysis-task.service';

@Component({
    selector: 'jhi-analysis-task-delete-dialog',
    templateUrl: './analysis-task-delete-dialog.component.html'
})
export class AnalysisTaskDeleteDialogComponent {

    analysisTask: AnalysisTask;

    constructor(
        private analysisTaskService: AnalysisTaskService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.analysisTaskService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'analysisTaskListModification',
                content: 'Deleted an analysisTask'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-analysis-task-delete-popup',
    template: ''
})
export class AnalysisTaskDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private analysisTaskPopupService: AnalysisTaskPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.analysisTaskPopupService
                .open(AnalysisTaskDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
