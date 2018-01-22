import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CameraComponent } from './camera.component';
import { CameraDetailComponent } from './camera-detail.component';
import { CameraPopupComponent } from './camera-dialog.component';
import { CameraDeletePopupComponent } from './camera-delete-dialog.component';

@Injectable()
export class CameraResolvePagingParams implements Resolve<any> {

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

export const cameraRoute: Routes = [
    {
        path: 'camera',
        component: CameraComponent,
        resolve: {
            'pagingParams': CameraResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.camera.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'camera/:id',
        component: CameraDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.camera.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cameraPopupRoute: Routes = [
    {
        path: 'camera-new',
        component: CameraPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.camera.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'camera/:id/edit',
        component: CameraPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.camera.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'camera/:id/delete',
        component: CameraDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ctecApp.camera.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
