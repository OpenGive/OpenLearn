import {User} from "./user.model";

export class Student extends User {
  public organizationId: number;
  public fourteenPlus: boolean;
  public guardianFirstName: string;
  public guardianLastName: string;
  public guardianEmail: string;
  public guardianPhone: string;
  public school: string;
  public gradeLevel: string; // TODO: Enum;
  public stateStudentId?: string;
  public orgStudentId?: string;
}
