import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Portfolio } from './portfolio.model';
import { PortfolioService } from './portfolio.service';
@Injectable()
export class PortfolioPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private portfolioService: PortfolioService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.portfolioService.find(id).subscribe(portfolio => {
                this.portfolioModalRef(component, portfolio);
            });
        } else {
            return this.portfolioModalRef(component, new Portfolio());
        }
    }

    portfolioModalRef(component: Component, portfolio: Portfolio): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.portfolio = portfolio;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
