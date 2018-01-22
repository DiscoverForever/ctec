/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { CtecTestModule } from '../../../test.module';
import { AlarmRegionDetailComponent } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region-detail.component';
import { AlarmRegionService } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region.service';
import { AlarmRegion } from '../../../../../../main/webapp/app/entities/alarm-region/alarm-region.model';

describe('Component Tests', () => {

    describe('AlarmRegion Management Detail Component', () => {
        let comp: AlarmRegionDetailComponent;
        let fixture: ComponentFixture<AlarmRegionDetailComponent>;
        let service: AlarmRegionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [AlarmRegionDetailComponent],
                providers: [
                    AlarmRegionService
                ]
            })
            .overrideTemplate(AlarmRegionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmRegionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmRegionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new AlarmRegion(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.alarmRegion).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
