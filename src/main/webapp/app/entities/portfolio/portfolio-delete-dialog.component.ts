import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Portfolio } from './portfolio.model';
import { PortfolioPopupService } from './portfolio-popup.service';
import { PortfolioService } from './portfolio.service';

@Component({
    selector: 'jhi-portfolio-delete-dialog',
    templateUrl: './portfolio-delete-dialog.component.html'
})
export class PortfolioDeleteDialogComponent {

    portfolio: Portfolio;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private portfolioService: PortfolioService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['portfolio']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.portfolioService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'portfolioListModification',
                content: 'Deleted an portfolio'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-portfolio-delete-popup',
    template: ''
})
export class PortfolioDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private portfolioPopupService: PortfolioPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.portfolioPopupService
                .open(PortfolioDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
