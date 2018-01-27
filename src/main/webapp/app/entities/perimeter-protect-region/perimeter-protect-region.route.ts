import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { PerimeterProtectRegionComponent } from './perimeter-protect-region.component';
import { PerimeterProtectRegionDetailComponent } from './perimeter-protect-region-detail.component';
import { PerimeterProtectRegionPopupComponent } from './perimeter-protect-region-dialog.component';
import { PerimeterProtectRegionDeletePopupComponent } from './perimeter-protect-region-delete-dialog.component';

@Injectable()
export class PerimeterProtectRegionResolvePagingParams implements Resolve<any> {

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

export const perimeterProtectRegionRoute: Routes = [
    {
        path: 'perimeter-protect-region',
        component: PerimeterProtectRegionComponent,
        resolve: {
            'pagingParams': PerimeterProtectRegionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.perimeterProtectRegion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'perimeter-protect-region/:id',
        component: PerimeterProtectRegionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.perimeterProtectRegion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const perimeterProtectRegionPopupRoute: Routes = [
    {
        path: 'perimeter-protect-region-new',
        component: PerimeterProtectRegionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.perimeterProtectRegion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'perimeter-protect-region/:id/edit',
        component: PerimeterProtectRegionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.perimeterProtectRegion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'perimeter-protect-region/:id/delete',
        component: PerimeterProtectRegionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.perimeterProtectRegion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
