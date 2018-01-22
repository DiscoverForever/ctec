import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { AlarmRegionComponent } from './alarm-region.component';
import { AlarmRegionDetailComponent } from './alarm-region-detail.component';
import { AlarmRegionPopupComponent } from './alarm-region-dialog.component';
import { AlarmRegionDeletePopupComponent } from './alarm-region-delete-dialog.component';

@Injectable()
export class AlarmRegionResolvePagingParams implements Resolve<any> {

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

export const alarmRegionRoute: Routes = [
    {
        path: 'alarm-region',
        component: AlarmRegionComponent,
        resolve: {
            'pagingParams': AlarmRegionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.alarmRegion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'alarm-region/:id',
        component: AlarmRegionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.alarmRegion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const alarmRegionPopupRoute: Routes = [
    {
        path: 'alarm-region-new',
        component: AlarmRegionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.alarmRegion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'alarm-region/:id/edit',
        component: AlarmRegionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.alarmRegion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'alarm-region/:id/delete',
        component: AlarmRegionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.alarmRegion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
