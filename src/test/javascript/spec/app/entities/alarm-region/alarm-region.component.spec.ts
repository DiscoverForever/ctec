/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { CtecTestModule } from '../../../test.module';
import { AlarmRegionComponent } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region.component';
import { AlarmRegionService } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region.service';
import { AlarmRegion } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region.model';

describe('Component Tests', () => {

    describe('AlarmRegion Management Component', () => {
        let comp: AlarmRegionComponent;
        let fixture: ComponentFixture<AlarmRegionComponent>;
        let service: AlarmRegionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [AlarmRegionComponent],
                providers: [
                    AlarmRegionService
                ]
            })
            .overrideTemplate(AlarmRegionComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmRegionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmRegionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new AlarmRegion(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.alarmRegions[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
