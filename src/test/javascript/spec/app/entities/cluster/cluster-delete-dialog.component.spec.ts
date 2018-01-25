/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CtecTestModule } from '../../../test.module';
import { ClusterDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/cluster/cluster-delete-dialog.component';
import { ClusterService } from '../../../../../../main/webapp/app/entities/cluster/cluster.service';

describe('Component Tests', () => {

    describe('Cluster Management Delete Component', () => {
        let comp: ClusterDeleteDialogComponent;
        let fixture: ComponentFixture<ClusterDeleteDialogComponent>;
        let service: ClusterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [ClusterDeleteDialogComponent],
                providers: [
                    ClusterService
                ]
            })
            .overrideTemplate(ClusterDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClusterDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClusterService);
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
