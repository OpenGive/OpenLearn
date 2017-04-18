import { Milestone } from '../milestone';
import { User } from '../../shared';
export class Achievement {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public badgeUrl?: string,
        public milestone?: Milestone,
        public achievedBy?: User,
    ) {
    }
}
