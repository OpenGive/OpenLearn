export class Course {
  public id?: number;
  public name: string;
  public description: string;
  public startDate: Date;
  public endDate: Date;
  public sessionId: number;
  public instructorId: number;
  public locations?: string;
  public times?: string;
}
