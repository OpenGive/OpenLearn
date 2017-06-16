import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { OpengiveTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MilestoneDetailComponent } from '../../../../../../main/webapp/app/entities/milestone/milestone-detail.component';
import { MilestoneService } from '../../../../../../main/webapp/app/entities/milestone/milestone.service';
import { Milestone } from '../../../../../../main/webapp/app/entities/milestone/milestone.model';

describe('Component Tests', () => {

    describe('Milestone Management Detail Component', () => {
        let comp: MilestoneDetailComponent;
        let fixture: ComponentFixture<MilestoneDetailComponent>;
        let service: MilestoneService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OpengiveTestModule],
                declarations: [MilestoneDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MilestoneService,
                    EventManager
                ]
            }).overrideComponent(MilestoneDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MilestoneDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MilestoneService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Milestone(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.milestone).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
