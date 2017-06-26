import {User} from "../shared/user/user.model";

export interface Course {
  name?: string;
  description?: string;
  startDate: Date;
  endDate?: Date;
  id?: number;
  instructor?: User;
}
