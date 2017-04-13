import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Achievement } from './achievement.model';
import { AchievementPopupService } from './achievement-popup.service';
import { AchievementService } from './achievement.service';

@Component({
    selector: 'jhi-achievement-delete-dialog',
    templateUrl: './achievement-delete-dialog.component.html'
})
export class AchievementDeleteDialogComponent {

    achievement: Achievement;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private achievementService: AchievementService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['achievement']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.achievementService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'achievementListModification',
                content: 'Deleted an achievement'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-achievement-delete-popup',
    template: ''
})
export class AchievementDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private achievementPopupService: AchievementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.achievementPopupService
                .open(AchievementDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
