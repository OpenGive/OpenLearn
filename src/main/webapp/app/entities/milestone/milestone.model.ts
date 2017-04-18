import { Program } from '../program';
import { Achievement } from '../achievement';
export class Milestone {
    constructor(
        public id?: number,
        public name?: string,
        public program?: Program,
        public achievement?: Achievement,
    ) {
    }
}
