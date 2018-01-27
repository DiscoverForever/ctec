/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { CtecTestModule } from '../../../test.module';
import { PerimeterProtectRegionComponent } from '../../../../../../main/webapp/app/entities/perimeter-protect-region/perimeter-protect-region.component';
import { PerimeterProtectRegionService } from '../../../../../../main/webapp/app/entities/perimeter-protect-region/perimeter-protect-region.service';
import { PerimeterProtectRegion } from '../../../../../../main/webapp/app/entities/perimeter-protect-region/perimeter-protect-region.model';

describe('Component Tests', () => {

    describe('PerimeterProtectRegion Management Component', () => {
        let comp: PerimeterProtectRegionComponent;
        let fixture: ComponentFixture<PerimeterProtectRegionComponent>;
        let service: PerimeterProtectRegionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [PerimeterProtectRegionComponent],
                providers: [
                    PerimeterProtectRegionService
                ]
            })
            .overrideTemplate(PerimeterProtectRegionComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PerimeterProtectRegionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PerimeterProtectRegionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new PerimeterProtectRegion(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.perimeterProtectRegions[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
