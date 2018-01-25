import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Cluster } from './cluster.model';
import { ClusterService } from './cluster.service';

@Component({
    selector: 'jhi-cluster-detail',
    templateUrl: './cluster-detail.component.html'
})
export class ClusterDetailComponent implements OnInit, OnDestroy {

    cluster: Cluster;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private clusterService: ClusterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInClusters();
    }

    load(id) {
        this.clusterService.find(id).subscribe((cluster) => {
            this.cluster = cluster;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInClusters() {
        this.eventSubscriber = this.eventManager.subscribe(
            'clusterListModification',
            (response) => this.load(this.cluster.id)
        );
    }
}
