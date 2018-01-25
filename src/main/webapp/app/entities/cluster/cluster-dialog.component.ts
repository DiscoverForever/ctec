import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cluster } from './cluster.model';
import { ClusterPopupService } from './cluster-popup.service';
import { ClusterService } from './cluster.service';

@Component({
    selector: 'jhi-cluster-dialog',
    templateUrl: './cluster-dialog.component.html'
})
export class ClusterDialogComponent implements OnInit {

    cluster: Cluster;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private clusterService: ClusterService,
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
        if (this.cluster.id !== undefined) {
            this.subscribeToSaveResponse(
                this.clusterService.update(this.cluster));
        } else {
            this.subscribeToSaveResponse(
                this.clusterService.create(this.cluster));
        }
    }

    private subscribeToSaveResponse(result: Observable<Cluster>) {
        result.subscribe((res: Cluster) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Cluster) {
        this.eventManager.broadcast({ name: 'clusterListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-cluster-popup',
    template: ''
})
export class ClusterPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private clusterPopupService: ClusterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.clusterPopupService
                    .open(ClusterDialogComponent as Component, params['id']);
            } else {
                this.clusterPopupService
                    .open(ClusterDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
