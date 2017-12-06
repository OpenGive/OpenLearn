import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import {Router} from "@angular/router";
import * as _ from "lodash";

import {Course} from '../../../models/course.model';
import {CourseDialogComponent} from "../course-dialog.component";
import {GradeDialogComponent} from "../grade-dialog.component";
import {StudentCourseService} from "../../../services/student-course.service";
import {Principal} from "../../../shared/auth/principal.service";
import {AppConstants} from "../../../app.constants";

@Component({
  selector: 'app-course-grid',
  templateUrl: './course-grid.component.html',
  styleUrls: ['./course-grid.component.css']
})
export class CourseGridComponent implements OnInit {

  @Input() course: Course;
  students: any[];
  columns: any[];

  sortColumn: any;
  reverse: boolean;
  instructorCheck: boolean = true;

  constructor(private router: Router,
              private dialog: MdDialog,
              private courseService: StudentCourseService,
              private principal: Principal) {}

  ngOnInit(): void {
    if (this.principal.hasAuthority(AppConstants.Role.Instructor.name)) this.instructorCheck = this.course.instructorId == this.principal.getId();
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
    this.stopPropagation(e);
    this.dialog.open(GradeDialogComponent, {
      data: {
        student: student,
        type: 'COURSE'
      },
      width: "50px",
      height: "200px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleEditGradeResponse(resp)
    });
  }

  removeStudent(id: Number): void {
    this.courseService.deleteStudentCourse(id).subscribe(resp => {
      this.students = _.filter(this.students, student => student.id !== id);
    });
  }

  getStudents(): void {
    this.courseService.getStudentCoursesByCourse(this.course.id).subscribe(students => {
      this.students = students;
    })
  }

  viewStudentDetails(student): void {
    this.router.navigate(['/students/' + student.id]);
  }

  stopPropagation(e): void {
    e.stopPropagation();
  }

   private handleEditStudentResponse(resp): void {
    if (resp) {
      console.log("Response from edit student", resp);

      for (var i = 0; i < this.students.length; i++) {
         let student = this.students[i];
         if (student.student.id == resp.data.id) {
           this.students[i].student = resp.data;
         }
      }
    }
  }

  private handleAddStudentResponse(resp): void {
    if (resp) {
      console.log("Response from add student", resp);

      this.ngOnInit();
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
