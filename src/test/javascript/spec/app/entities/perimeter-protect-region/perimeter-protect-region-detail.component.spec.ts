/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { CtecTestModule } from '../../../test.module';
import { PerimeterProtectRegionDetailComponent } from '../../../../../../main/webapp/app/entities/perimeter-protect-region/perimeter-protect-region-detail.component';
import { PerimeterProtectRegionService } from '../../../../../../main/webapp/app/entities/perimeter-protect-region/perimeter-protect-region.service';
import { PerimeterProtectRegion } from '../../../../../../main/webapp/app/entities/perimeter-protect-region/perimeter-protect-region.model';

describe('Component Tests', () => {

    describe('PerimeterProtectRegion Management Detail Component', () => {
        let comp: PerimeterProtectRegionDetailComponent;
        let fixture: ComponentFixture<PerimeterProtectRegionDetailComponent>;
        let service: PerimeterProtectRegionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [PerimeterProtectRegionDetailComponent],
                providers: [
                    PerimeterProtectRegionService
                ]
            })
            .overrideTemplate(PerimeterProtectRegionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PerimeterProtectRegionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PerimeterProtectRegionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new PerimeterProtectRegion(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.perimeterProtectRegion).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
