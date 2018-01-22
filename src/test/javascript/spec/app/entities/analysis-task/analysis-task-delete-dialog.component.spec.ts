/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CtecTestModule } from '../../../test.module';
import { AnalysisTaskDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task-delete-dialog.component';
import { AnalysisTaskService } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task.service';

describe('Component Tests', () => {

    describe('AnalysisTask Management Delete Component', () => {
        let comp: AnalysisTaskDeleteDialogComponent;
        let fixture: ComponentFixture<AnalysisTaskDeleteDialogComponent>;
        let service: AnalysisTaskService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [AnalysisTaskDeleteDialogComponent],
                providers: [
                    AnalysisTaskService
                ]
            })
            .overrideTemplate(AnalysisTaskDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AnalysisTaskDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalysisTaskService);
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
