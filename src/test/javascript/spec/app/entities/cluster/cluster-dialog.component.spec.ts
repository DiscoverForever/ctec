/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CtecTestModule } from '../../../test.module';
import { ClusterDialogComponent } from '../../../../../../main/webapp/app/entities/cluster/cluster-dialog.component';
import { ClusterService } from '../../../../../../main/webapp/app/entities/cluster/cluster.service';
import { Cluster } from '../../../../../../main/webapp/app/entities/cluster/cluster.model';

describe('Component Tests', () => {

    describe('Cluster Management Dialog Component', () => {
        let comp: ClusterDialogComponent;
        let fixture: ComponentFixture<ClusterDialogComponent>;
        let service: ClusterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [ClusterDialogComponent],
                providers: [
                    ClusterService
                ]
            })
            .overrideTemplate(ClusterDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClusterDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClusterService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Cluster(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.cluster = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'clusterListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Cluster();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.cluster = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'clusterListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
