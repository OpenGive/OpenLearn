import { Instructor }   from './instructor';
import { Activity } from './activity';

export interface Course {
    name?: string;
    description?: string;
    startDate: Date;
    endDate?: Date;
    id?: number;
    instructor?: Instructor;
    activities?: Activity[];
}
