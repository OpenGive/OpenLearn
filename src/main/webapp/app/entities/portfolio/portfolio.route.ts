import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { PortfolioComponent } from './portfolio.component';
import { PortfolioDetailComponent } from './portfolio-detail.component';
import { PortfolioPopupComponent } from './portfolio-dialog.component';
import { PortfolioDeletePopupComponent } from './portfolio-delete-dialog.component';

import { Principal } from '../../shared';


export const portfolioRoute: Routes = [
  {
    path: 'portfolio',
    component: PortfolioComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'opengiveApp.portfolio.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'portfolio/:id',
    component: PortfolioDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'opengiveApp.portfolio.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const portfolioPopupRoute: Routes = [
  {
    path: 'portfolio-new',
    component: PortfolioPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'opengiveApp.portfolio.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'portfolio/:id/edit',
    component: PortfolioPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'opengiveApp.portfolio.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'portfolio/:id/delete',
    component: PortfolioDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'opengiveApp.portfolio.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
