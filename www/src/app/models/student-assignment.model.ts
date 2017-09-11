import {Student} from "./student.model";
import {Assignment} from "./assignment.model";

export class StudentAssignment {
  public id?: number;
  public studentId: number;
  public assignmentId: number;
  public student?: Student;
  public assignment?: Assignment;
  public grade: string;
  public complete?: boolean;
  public onPortfolio?: boolean;
}
