import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {Course} from '../../../models/course.model';
import {CourseDialogComponent} from "../course-dialog.component";
import {GradeDialogComponent} from "../grade-dialog.component";
import {FileUploadComponent} from "../../fileupload/fileupload.component";
import {CourseStudentDialogComponent} from "../course-student-dialog.component";
import {StudentCourseService} from "../../../services/student-course.service";
import {DataService} from "../../../services/data.service";

@Component({
  selector: 'app-course-files-grid',
  templateUrl: './course-files-grid.component.html',
  styleUrls: ['./course-files-grid.component.css']
})
export class CourseFilesGridComponent implements OnInit {

  @Input() course: Course;
  files: any[];
  columns: any[];

  sortColumn: any;
  reverse: boolean;

  constructor(private dialog: MdDialog,
              private dataService: DataService,
              private courseService: StudentCourseService) {}

  ngOnInit(): void {

    this.columns = [
      {
        id: "key",
        name: "Name"
      },
      {
        id: "size",
        name: "Size"
      },
      {
        id: "lastModified",
        name: "Date"
      }
    ];

    this.getFiles();

  }

  add(): void {
    this.dialog.open(FileUploadComponent, {
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
      this.files = _.filter(this.files, files => files.id !== id);
    });
  }

  getFiles(): void {
    this.courseService.getCourseFiles(this.course.id).subscribe(files => {
      this.files = files;
    })
  }

  viewStudentDetails(student): void {
    this.dataService.setStudentById(student.id);
  }

  stopPropagation(e): void {
    e.stopPropagation();
  }

   private handleEditStudentResponse(resp): void {
    if (resp) {
      console.log("Response from edit student", resp);

      for (var i = 0; i < this.files.length; i++) {
         let student = this.files[i];
         if (student.student.id == resp.data.id) {
           this.files[i].student = resp.data;
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

      for (var i = 0; i < this.files.length; i++) {
         let student = this.files[i];
         if (student.student.id == resp.data.student.id) {
           student.grade = resp.data.grade;
         }
      }

    }
  }
}
