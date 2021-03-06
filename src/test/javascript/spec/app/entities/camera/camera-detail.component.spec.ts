/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { CtecTestModule } from '../../../test.module';
import { CameraDetailComponent } from '../../../../../../main/webapp/app/entities/camera/camera-detail.component';
import { CameraService } from '../../../../../../main/webapp/app/entities/camera/camera.service';
import { Camera } from '../../../../../../main/webapp/app/entities/camera/camera.model';

describe('Component Tests', () => {

    describe('Camera Management Detail Component', () => {
        let comp: CameraDetailComponent;
        let fixture: ComponentFixture<CameraDetailComponent>;
        let service: CameraService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [CameraDetailComponent],
                providers: [
                    CameraService
                ]
            })
            .overrideTemplate(CameraDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CameraDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CameraService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Camera(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.camera).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
