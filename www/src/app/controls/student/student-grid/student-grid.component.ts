import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {Course} from '../../../models/course.model';
import {StudentDialogComponent} from "../student-dialog.component";
import {StudentGradeDialogComponent} from "../grade-dialog.component";
import {CourseStudentDialogComponent} from "../course-student-dialog.component";
import {StudentCourseService} from "../../../services/student-course.service";
import {Student} from "../../../models/student.model";

@Component({
  selector: 'app-student-grid',
  templateUrl: './student-grid.component.html',
  styleUrls: ['./student-grid.component.css']
})
export class StudentGridComponent implements OnInit {

  @Input() student: Student;
  courses: any[];
  columns: any[];

  sortColumn: any;
  reverse: boolean;

  constructor(private dialog: MdDialog,
              private courseService: StudentCourseService) {}

  ngOnInit(): void {

    this.columns = [
      {
        id: "course.name",
        name: "Name"
      },
      {
        id: "course.description",
        name: "Description"
      },
      {
        id: "grade",
        name: "Grade"
      }
    ];

    this.getCourses();

  }

  add(): void {
    this.dialog.open(StudentDialogComponent, {
      data: {
        course: this.student
      },
      width: "400px",
      height: "600px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleAddStudentResponse(resp)
    });
  }

  editGrade(course, e): void {
    this.stopPropagation(e);
    this.dialog.open(StudentGradeDialogComponent, {
      data: {
        course: course,
      },
      width: "50px",
      height: "200px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
       this.handleEditGradeResponse(resp)
    });
  }

  // removeStudent(id: Number): void {
  //   this.courseService.deleteStudentCourse(id).subscribe(resp => {
  //     this.students = _.filter(this.students, student => student.id !== id);
  //   });
  // }

  getCourses(): void {
    this.courseService.getStudentCoursesByStudent(this.student.id).subscribe(courses => {
      this.courses = courses;
      console.log(courses);
    })
  }

  viewCourseDetails(course): void {
    this.dialog.open(CourseStudentDialogComponent,  {
      data: {
        item: course,
          adding: false
      },
      disableClose: true
    }).afterClosed().subscribe(resp => {

    });
  }

  stopPropagation(e): void {
    e.stopPropagation();
  }


  private handleAddStudentResponse(resp): void {
    if (resp) {
      console.log("Response from add student", resp);

      var studentData = resp.data;

      this.courses.push(studentData);
    }
  }

  private handleEditGradeResponse(resp): void {
    console.log(resp);
  }
}
