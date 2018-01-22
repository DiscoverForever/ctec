/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CtecTestModule } from '../../../test.module';
import { AnalysisTaskDialogComponent } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task-dialog.component';
import { AnalysisTaskService } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task.service';
import { AnalysisTask } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task.model';

describe('Component Tests', () => {

    describe('AnalysisTask Management Dialog Component', () => {
        let comp: AnalysisTaskDialogComponent;
        let fixture: ComponentFixture<AnalysisTaskDialogComponent>;
        let service: AnalysisTaskService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [AnalysisTaskDialogComponent],
                providers: [
                    AnalysisTaskService
                ]
            })
            .overrideTemplate(AnalysisTaskDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AnalysisTaskDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalysisTaskService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new AnalysisTask(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.analysisTask = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'analysisTaskListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new AnalysisTask();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.analysisTask = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'analysisTaskListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
