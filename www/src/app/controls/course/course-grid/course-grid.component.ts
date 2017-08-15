import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {Course} from '../../../models/course';
import {CourseDialogComponent} from "../course-dialog.component";
import {GradeDialogComponent} from "../grade-dialog.component";
import {CourseStudentDialogComponent} from "../course-student-dialog.component";
import {CourseService} from "../../../services/course.service";

@Component({
  selector: 'app-course-grid',
  templateUrl: './course-grid.component.html',
  styleUrls: ['./course-grid.component.css']
})
export class CourseGridComponent implements OnInit {

  @Input() course: Course;
  students: any[];
  columns: any[]

  sortColumn: any;
  reverse: boolean;

  constructor(private dialog: MdDialog,
              private courseService: CourseService) {}

  ngOnInit(): void {

    this.columns = [
      {
        id: "student.firstName",
        name: "First Name"
      },
      {
        id: "student.lastName",
        name: "Last Name"
      },
      {
        id: "grade",
        name: "Grade"
      }
    ];

    this.getStudents();

  }

  add(): void {
    this.dialog.open(CourseDialogComponent, {
      data: {
        course: this.course
      },
      width: "400px",
      height: "600px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleAddStudentResponse(resp)
    });
  }

  editGrade(student, e): void {
    e.stopPropagation();
    this.dialog.open(GradeDialogComponent, {
      data: {
        course: this.course,
        student: student
      },
      width: "50px",
      height: "200px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleEditGradeResponse(resp)
    });
  }

  removeStudent(studentId: Number): void {
    this.courseService.removeStudentFromCourse(this.course.id, studentId).subscribe(students => {
      this.students = this.students.filter(student => student.student.id !== studentId);
    })
  }

  getStudents(): void {
    this.courseService.getCourseStudents(this.course.id).subscribe(students => {
      this.students = students;
    })
  }

  viewStudentDetails(student): void {
    this.dialog.open(CourseStudentDialogComponent, {
      data: {
        item: student,
        adding: false
      },
      disableClose: true
    });
    // Handle response
    //.afterClosed().subscribe(resp => {
    //this.handleDialogResponse(resp)
    //});
  }

  private handleAddStudentResponse(resp): void {
    if (resp) {
      console.log("Response from add student", resp);

      var studentData = resp.data;

      this.students.push(studentData);
    }
  }

  private handleEditGradeResponse(resp): void {
    if (resp) {
      console.log("Response from edit grade", resp);

      for (var i = 0; i < this.students.length; i++) {
         let student = this.students[i];
         if (student.student.id == resp.data.student.id) {
           student.grade = resp.data.grade;
         }
      }

    }
  }
}
