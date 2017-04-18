import { User } from '../../shared';
import { ItemLink } from '../item-link';
export class Portfolio {
    constructor(
        public id?: number,
        public student?: User,
        public portfolioItems?: ItemLink,
    ) {
    }
}
