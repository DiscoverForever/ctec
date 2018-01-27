/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CtecTestModule } from '../../../test.module';
import { PerimeterProtectRegionDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/perimeter-protect-region/perimeter-protect-region-delete-dialog.component';
import { PerimeterProtectRegionService } from '../../../../../../main/webapp/app/entities/perimeter-protect-region/perimeter-protect-region.service';

describe('Component Tests', () => {

    describe('PerimeterProtectRegion Management Delete Component', () => {
        let comp: PerimeterProtectRegionDeleteDialogComponent;
        let fixture: ComponentFixture<PerimeterProtectRegionDeleteDialogComponent>;
        let service: PerimeterProtectRegionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [PerimeterProtectRegionDeleteDialogComponent],
                providers: [
                    PerimeterProtectRegionService
                ]
            })
            .overrideTemplate(PerimeterProtectRegionDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PerimeterProtectRegionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PerimeterProtectRegionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
