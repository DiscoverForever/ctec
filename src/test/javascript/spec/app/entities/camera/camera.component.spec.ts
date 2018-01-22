/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { CtecTestModule } from '../../../test.module';
import { CameraComponent } from '../../../../../../main/webapp/app/entities/camera/camera.component';
import { CameraService } from '../../../../../../main/webapp/app/entities/camera/camera.service';
import { Camera } from '../../../../../../main/webapp/app/entities/camera/camera.model';

describe('Component Tests', () => {

    describe('Camera Management Component', () => {
        let comp: CameraComponent;
        let fixture: ComponentFixture<CameraComponent>;
        let service: CameraService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [CameraComponent],
                providers: [
                    CameraService
                ]
            })
            .overrideTemplate(CameraComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CameraComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CameraService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Camera(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.cameras[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
