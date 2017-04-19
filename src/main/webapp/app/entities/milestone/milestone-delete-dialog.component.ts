import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Milestone } from './milestone.model';
import { MilestonePopupService } from './milestone-popup.service';
import { MilestoneService } from './milestone.service';

@Component({
    selector: 'jhi-milestone-delete-dialog',
    templateUrl: './milestone-delete-dialog.component.html'
})
export class MilestoneDeleteDialogComponent {

    milestone: Milestone;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private milestoneService: MilestoneService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['milestone']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.milestoneService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'milestoneListModification',
                content: 'Deleted an milestone'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-milestone-delete-popup',
    template: ''
})
export class MilestoneDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private milestonePopupService: MilestonePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.milestonePopupService
                .open(MilestoneDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
