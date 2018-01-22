import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { AnalysisTask } from './analysis-task.model';
import { AnalysisTaskService } from './analysis-task.service';

@Injectable()
export class AnalysisTaskPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private analysisTaskService: AnalysisTaskService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.analysisTaskService.find(id).subscribe((analysisTask) => {
                    analysisTask.createdAt = this.datePipe
                        .transform(analysisTask.createdAt, 'yyyy-MM-ddTHH:mm:ss');
                    analysisTask.executionAt = this.datePipe
                        .transform(analysisTask.executionAt, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.analysisTaskModalRef(component, analysisTask);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.analysisTaskModalRef(component, new AnalysisTask());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    analysisTaskModalRef(component: Component, analysisTask: AnalysisTask): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.analysisTask = analysisTask;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
