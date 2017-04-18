import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Organization } from './organization.model';
import { OrganizationPopupService } from './organization-popup.service';
import { OrganizationService } from './organization.service';

@Component({
    selector: 'jhi-organization-delete-dialog',
    templateUrl: './organization-delete-dialog.component.html'
})
export class OrganizationDeleteDialogComponent {

    organization: Organization;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private organizationService: OrganizationService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['organization']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.organizationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'organizationListModification',
                content: 'Deleted an organization'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-organization-delete-popup',
    template: ''
})
export class OrganizationDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private organizationPopupService: OrganizationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.organizationPopupService
                .open(OrganizationDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
