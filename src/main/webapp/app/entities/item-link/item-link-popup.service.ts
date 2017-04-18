import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ItemLink } from './item-link.model';
import { ItemLinkService } from './item-link.service';
@Injectable()
export class ItemLinkPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private itemLinkService: ItemLinkService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.itemLinkService.find(id).subscribe(itemLink => {
                this.itemLinkModalRef(component, itemLink);
            });
        } else {
            return this.itemLinkModalRef(component, new ItemLink());
        }
    }

    itemLinkModalRef(component: Component, itemLink: ItemLink): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.itemLink = itemLink;
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
