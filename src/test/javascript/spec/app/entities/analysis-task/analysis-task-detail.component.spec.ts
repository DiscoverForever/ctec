/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { CtecTestModule } from '../../../test.module';
import { AnalysisTaskDetailComponent } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task-detail.component';
import { AnalysisTaskService } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task.service';
import { AnalysisTask } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task.model';

describe('Component Tests', () => {

    describe('AnalysisTask Management Detail Component', () => {
        let comp: AnalysisTaskDetailComponent;
        let fixture: ComponentFixture<AnalysisTaskDetailComponent>;
        let service: AnalysisTaskService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [AnalysisTaskDetailComponent],
                providers: [
                    AnalysisTaskService
                ]
            })
            .overrideTemplate(AnalysisTaskDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AnalysisTaskDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalysisTaskService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new AnalysisTask(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.analysisTask).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
