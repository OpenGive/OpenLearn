import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { OpengiveTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PortfolioDetailComponent } from '../../../../../../main/webapp/app/entities/portfolio/portfolio-detail.component';
import { PortfolioService } from '../../../../../../main/webapp/app/entities/portfolio/portfolio.service';
import { Portfolio } from '../../../../../../main/webapp/app/entities/portfolio/portfolio.model';

describe('Component Tests', () => {

    describe('Portfolio Management Detail Component', () => {
        let comp: PortfolioDetailComponent;
        let fixture: ComponentFixture<PortfolioDetailComponent>;
        let service: PortfolioService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OpengiveTestModule],
                declarations: [PortfolioDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PortfolioService,
                    EventManager
                ]
            }).overrideComponent(PortfolioDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PortfolioDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PortfolioService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Portfolio(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.portfolio).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
