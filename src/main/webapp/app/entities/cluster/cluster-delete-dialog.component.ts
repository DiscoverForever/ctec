import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cluster } from './cluster.model';
import { ClusterPopupService } from './cluster-popup.service';
import { ClusterService } from './cluster.service';

@Component({
    selector: 'jhi-cluster-delete-dialog',
    templateUrl: './cluster-delete-dialog.component.html'
})
export class ClusterDeleteDialogComponent {

    cluster: Cluster;

    constructor(
        private clusterService: ClusterService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.clusterService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'clusterListModification',
                content: 'Deleted an cluster'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cluster-delete-popup',
    template: ''
})
export class ClusterDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private clusterPopupService: ClusterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.clusterPopupService
                .open(ClusterDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
