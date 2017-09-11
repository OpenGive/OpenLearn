import {Student} from "./student.model";
import {Course} from "./course.model";

export class StudentCourse {
  public id?: number;
  public studentId: number;
  public courseId: number;
  public student?: Student;
  public course?: Course;
  public grade: string;
  public enrollDate?: Date;
  public dropDate?: Date;
  public complete?: boolean;
  public onPortfolio?: boolean;
}
