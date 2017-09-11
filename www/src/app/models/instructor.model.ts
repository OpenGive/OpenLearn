import {User} from "./user.model";

export class Instructor extends User {
  public organizationId: number;
  public orgRole: string;
}
