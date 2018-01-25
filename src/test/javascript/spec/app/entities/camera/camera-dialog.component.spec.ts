/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CtecTestModule } from '../../../test.module';
import { CameraDialogComponent } from '../../../../../../main/webapp/app/entities/camera/camera-dialog.component';
import { CameraService } from '../../../../../../main/webapp/app/entities/camera/camera.service';
import { Camera } from '../../../../../../main/webapp/app/entities/camera/camera.model';
import { AlarmRegionService } from '../../../../../../main/webapp/app/entities/alarm-region';
import { PerimeterProtectRegionService } from '../../../../../../main/webapp/app/entities/perimeter-protect-region';

describe('Component Tests', () => {

    describe('Camera Management Dialog Component', () => {
        let comp: CameraDialogComponent;
        let fixture: ComponentFixture<CameraDialogComponent>;
        let service: CameraService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [CameraDialogComponent],
                providers: [
                    AlarmRegionService,
                    PerimeterProtectRegionService,
                    CameraService
                ]
            })
            .overrideTemplate(CameraDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CameraDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CameraService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Camera(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.camera = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cameraListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Camera();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.camera = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cameraListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
