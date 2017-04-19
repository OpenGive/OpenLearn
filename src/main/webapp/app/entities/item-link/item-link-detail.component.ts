import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { ItemLink } from './item-link.model';
import { ItemLinkService } from './item-link.service';

@Component({
    selector: 'jhi-item-link-detail',
    templateUrl: './item-link-detail.component.html'
})
export class ItemLinkDetailComponent implements OnInit, OnDestroy {

    itemLink: ItemLink;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private itemLinkService: ItemLinkService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['itemLink']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInItemLinks();
    }

    load(id) {
        this.itemLinkService.find(id).subscribe((itemLink) => {
            this.itemLink = itemLink;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInItemLinks() {
        this.eventSubscriber = this.eventManager.subscribe('itemLinkListModification', (response) => this.load(this.itemLink.id));
    }
}
