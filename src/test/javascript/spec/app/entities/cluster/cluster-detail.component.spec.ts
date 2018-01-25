/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { CtecTestModule } from '../../../test.module';
import { ClusterDetailComponent } from '../../../../../../main/webapp/app/entities/cluster/cluster-detail.component';
import { ClusterService } from '../../../../../../main/webapp/app/entities/cluster/cluster.service';
import { Cluster } from '../../../../../../main/webapp/app/entities/cluster/cluster.model';

describe('Component Tests', () => {

    describe('Cluster Management Detail Component', () => {
        let comp: ClusterDetailComponent;
        let fixture: ComponentFixture<ClusterDetailComponent>;
        let service: ClusterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [ClusterDetailComponent],
                providers: [
                    ClusterService
                ]
            })
            .overrideTemplate(ClusterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClusterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClusterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Cluster(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.cluster).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
