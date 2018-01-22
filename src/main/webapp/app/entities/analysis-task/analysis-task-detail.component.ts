import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { AnalysisTask } from './analysis-task.model';
import { AnalysisTaskService } from './analysis-task.service';

@Component({
    selector: 'jhi-analysis-task-detail',
    templateUrl: './analysis-task-detail.component.html'
})
export class AnalysisTaskDetailComponent implements OnInit, OnDestroy {

    analysisTask: AnalysisTask;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private analysisTaskService: AnalysisTaskService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAnalysisTasks();
    }

    load(id) {
        this.analysisTaskService.find(id).subscribe((analysisTask) => {
            this.analysisTask = analysisTask;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAnalysisTasks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'analysisTaskListModification',
            (response) => this.load(this.analysisTask.id)
        );
    }
}
