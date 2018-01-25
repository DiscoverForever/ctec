import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ClusterComponent } from './cluster.component';
import { ClusterDetailComponent } from './cluster-detail.component';
import { ClusterPopupComponent } from './cluster-dialog.component';
import { ClusterDeletePopupComponent } from './cluster-delete-dialog.component';

@Injectable()
export class ClusterResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const clusterRoute: Routes = [
    {
        path: 'cluster',
        component: ClusterComponent,
        resolve: {
            'pagingParams': ClusterResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.cluster.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cluster/:id',
        component: ClusterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.cluster.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const clusterPopupRoute: Routes = [
    {
        path: 'cluster-new',
        component: ClusterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.cluster.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cluster/:id/edit',
        component: ClusterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.cluster.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cluster/:id/delete',
        component: ClusterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.cluster.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
