import {User} from "./user.model";

export class OrgAdmin extends User {
  public organizationId: number;
  public orgRole: string;
}
