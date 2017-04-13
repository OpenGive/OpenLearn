import { Program } from '../program';
import { User } from '../../shared';
export class Organization {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public programs?: Program,
        public users?: User,
    ) {
    }
}
