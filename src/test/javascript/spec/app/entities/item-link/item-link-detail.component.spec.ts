import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { OpengiveTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ItemLinkDetailComponent } from '../../../../../../main/webapp/app/entities/item-link/item-link-detail.component';
import { ItemLinkService } from '../../../../../../main/webapp/app/entities/item-link/item-link.service';
import { ItemLink } from '../../../../../../main/webapp/app/entities/item-link/item-link.model';

describe('Component Tests', () => {

    describe('ItemLink Management Detail Component', () => {
        let comp: ItemLinkDetailComponent;
        let fixture: ComponentFixture<ItemLinkDetailComponent>;
        let service: ItemLinkService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OpengiveTestModule],
                declarations: [ItemLinkDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ItemLinkService,
                    EventManager
                ]
            }).overrideComponent(ItemLinkDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ItemLinkDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItemLinkService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ItemLink(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.itemLink).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
