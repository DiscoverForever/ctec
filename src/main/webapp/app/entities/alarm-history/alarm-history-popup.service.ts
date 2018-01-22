import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { AlarmHistory } from './alarm-history.model';
import { AlarmHistoryService } from './alarm-history.service';

@Injectable()
export class AlarmHistoryPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private alarmHistoryService: AlarmHistoryService

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
                this.alarmHistoryService.find(id).subscribe((alarmHistory) => {
                    alarmHistory.time = this.datePipe
                        .transform(alarmHistory.time, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.alarmHistoryModalRef(component, alarmHistory);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.alarmHistoryModalRef(component, new AlarmHistory());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    alarmHistoryModalRef(component: Component, alarmHistory: AlarmHistory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.alarmHistory = alarmHistory;
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
