/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CtecTestModule } from '../../../test.module';
import { AlarmRegionDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region-delete-dialog.component';
import { AlarmRegionService } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region.service';

describe('Component Tests', () => {

    describe('AlarmRegion Management Delete Component', () => {
        let comp: AlarmRegionDeleteDialogComponent;
        let fixture: ComponentFixture<AlarmRegionDeleteDialogComponent>;
        let service: AlarmRegionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [AlarmRegionDeleteDialogComponent],
                providers: [
                    AlarmRegionService
                ]
            })
            .overrideTemplate(AlarmRegionDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmRegionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmRegionService);
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
