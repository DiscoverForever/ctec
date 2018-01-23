/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CtecTestModule } from '../../../test.module';
import { AlarmRegionDialogComponent } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region-dialog.component';
import { AlarmRegionService } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region.service';
import { AlarmRegion } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region.model';
import { CameraService } from '../../../../../../main/webapp/app/entities/camera';

describe('Component Tests', () => {

    describe('AlarmRegion Management Dialog Component', () => {
        let comp: AlarmRegionDialogComponent;
        let fixture: ComponentFixture<AlarmRegionDialogComponent>;
        let service: AlarmRegionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [AlarmRegionDialogComponent],
                providers: [
                    CameraService,
                    AlarmRegionService
                ]
            })
            .overrideTemplate(AlarmRegionDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmRegionDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmRegionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new AlarmRegion(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.alarmRegion = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'alarmRegionListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new AlarmRegion();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.alarmRegion = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'alarmRegionListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
