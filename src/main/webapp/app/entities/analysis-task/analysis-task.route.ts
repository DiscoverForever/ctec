import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { AnalysisTaskComponent } from './analysis-task.component';
import { AnalysisTaskDetailComponent } from './analysis-task-detail.component';
import { AnalysisTaskPopupComponent } from './analysis-task-dialog.component';
import { AnalysisTaskDeletePopupComponent } from './analysis-task-delete-dialog.component';

@Injectable()
export class AnalysisTaskResolvePagingParams implements Resolve<any> {

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

export const analysisTaskRoute: Routes = [
    {
        path: 'analysis-task',
        component: AnalysisTaskComponent,
        resolve: {
            'pagingParams': AnalysisTaskResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.analysisTask.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'analysis-task/:id',
        component: AnalysisTaskDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.analysisTask.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const analysisTaskPopupRoute: Routes = [
    {
        path: 'analysis-task-new',
        component: AnalysisTaskPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.analysisTask.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'analysis-task/:id/edit',
        component: AnalysisTaskPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.analysisTask.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'analysis-task/:id/delete',
        component: AnalysisTaskDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.analysisTask.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
