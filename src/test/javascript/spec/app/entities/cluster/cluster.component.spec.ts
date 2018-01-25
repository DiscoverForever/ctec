/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { CtecTestModule } from '../../../test.module';
import { ClusterComponent } from '../../../../../../main/webapp/app/entities/cluster/cluster.component';
import { ClusterService } from '../../../../../../main/webapp/app/entities/cluster/cluster.service';
import { Cluster } from '../../../../../../main/webapp/app/entities/cluster/cluster.model';

describe('Component Tests', () => {

    describe('Cluster Management Component', () => {
        let comp: ClusterComponent;
        let fixture: ComponentFixture<ClusterComponent>;
        let service: ClusterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CtecTestModule],
                declarations: [ClusterComponent],
                providers: [
                    ClusterService
                ]
            })
            .overrideTemplate(ClusterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClusterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClusterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Cluster(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.clusters[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
