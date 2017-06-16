import { Instructor }   from './instructor';
import { Milestone } from './milestone';

export interface Course {
  name?: string;
  description?: string;
  startDate: Date;
  endDate?: Date;
  id?: number;
  instructor?: Instructor;
  milestones?: Milestone[];
}
