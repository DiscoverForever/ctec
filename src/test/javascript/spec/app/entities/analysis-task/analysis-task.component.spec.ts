/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { CtecTestModule } from '../../../test.module';
import { AnalysisTaskComponent } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task.component';
import { AnalysisTaskService } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task.service';
import { AnalysisTask } from '../../../../../../main/webapp/app/entities/analysis-task/analysis-task.model';

describe('Component Tests', () => {

    describe('AnalysisTask Management Component', () => {
        let comp: AnalysisTaskComponent;
        let fixture: ComponentFixture<AnalysisTaskComponent>;
        let service: AnalysisTaskService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [AnalysisTaskComponent],
                providers: [
                    AnalysisTaskService
                ]
            })
            .overrideTemplate(AnalysisTaskComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AnalysisTaskComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalysisTaskService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new AnalysisTask(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.analysisTasks[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
