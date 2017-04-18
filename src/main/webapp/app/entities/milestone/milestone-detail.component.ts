import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Milestone } from './milestone.model';
import { MilestoneService } from './milestone.service';

@Component({
    selector: 'jhi-milestone-detail',
    templateUrl: './milestone-detail.component.html'
})
export class MilestoneDetailComponent implements OnInit, OnDestroy {

    milestone: Milestone;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private milestoneService: MilestoneService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['milestone']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMilestones();
    }

    load(id) {
        this.milestoneService.find(id).subscribe((milestone) => {
            this.milestone = milestone;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMilestones() {
        this.eventSubscriber = this.eventManager.subscribe('milestoneListModification', (response) => this.load(this.milestone.id));
    }
}
