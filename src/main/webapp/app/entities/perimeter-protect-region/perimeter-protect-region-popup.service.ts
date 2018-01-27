import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PerimeterProtectRegion } from './perimeter-protect-region.model';
import { PerimeterProtectRegionService } from './perimeter-protect-region.service';

@Injectable()
export class PerimeterProtectRegionPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private perimeterProtectRegionService: PerimeterProtectRegionService

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
                this.perimeterProtectRegionService.find(id).subscribe((perimeterProtectRegion) => {
                    this.ngbModalRef = this.perimeterProtectRegionModalRef(component, perimeterProtectRegion);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.perimeterProtectRegionModalRef(component, new PerimeterProtectRegion());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    perimeterProtectRegionModalRef(component: Component, perimeterProtectRegion: PerimeterProtectRegion): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.perimeterProtectRegion = perimeterProtectRegion;
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
