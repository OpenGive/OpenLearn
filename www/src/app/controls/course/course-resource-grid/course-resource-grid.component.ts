import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {Course} from '../../../models/course.model';
import {StudentCourseService} from "../../../services/student-course.service";
import {AssignmentService} from "../../../services/assignment.service";
import {AssignmentFormComponent} from "../assignment/assignment-form.component";
import {AdminTabs} from "../../admin/admin.constants";
import {AdminService} from "../../../services/admin.service";
import {Principal} from "../../../shared/auth/principal-storage.service";
import {AppConstants} from "../../../app.constants";

@Component({
  selector: 'app-course-resource-grid',
  templateUrl: './course-resource-grid.component.html',
  styleUrls: ['../course-grid/course-grid.component.css']
})
export class CourseResourceGridComponent implements OnInit {

  @Input() course: Course;
  @Input() assignment: any;
  assignments: any[];
  columns: any[];

  sortColumn: any;
  reverse: boolean;
  studentView: boolean;
  instructorCheck: boolean = true;

  constructor(private dialog: MdDialog,
              private courseService: StudentCourseService,
              private assignmentService: AssignmentService,
              private adminService: AdminService,
              private principal: Principal) {}
  ngOnInit(): void {
    this.studentView = this.principal.hasAuthority(AppConstants.Role.Student.name);
    if (this.principal.hasAuthority(AppConstants.Role.Instructor.name)) this.instructorCheck = this.course.instructorId == this.principal.getId();
    if(!this.studentView) {
      this.columns = [
        {
          id: "assignment.name",
          name: "Name"
        },
        {
          id: "assignment.description",
          name: "Description"
        }
      ];
    } else {
      this.columns = [
        {
          id: "assignment.name",
          name: "Name"
        },
        {
          id: "assignment.description",
          name: "Description"
        },
        {
          id: "assignment.grade",
          name: "Grade"
        }
      ];
    }

    this.getCourseAssignments();
  }

  add(): void {
    console.log('added');
    this.dialog.open(AssignmentFormComponent, {
      data: {
        course: this.course,
        adding: true,
        assignment: {},
        studentView: this.studentView
      },
      width: "400px",
      height: "600px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleDialogResponse(resp);
    });
  }

  viewAssignmentDetails(row){
    this.dialog.open(AssignmentFormComponent, {
      data: {
        course: this.course,
        adding: false,
        assignment: this.getAssignment(row),
        studentView: this.studentView
      },
      width: "600px",
      height: "600px"
    }).afterClosed().subscribe(resp => {
      this.handleDialogResponse(resp);
    })
}

  removeAssignment(assignmentId: Number): void {
    this.adminService.delete(AdminTabs.Assignment.route,assignmentId).subscribe(resp=> {
      this.assignments = _.filter(this.assignments, assignment => assignment.id !== assignmentId)
    })
  }

  getCourseAssignments(): void {
    if(!this.studentView) {
      this.assignmentService.getAssignmentsByCourse(this.course.id).subscribe(assignments => {
        this.assignments = assignments;
      })
    } else {
      this.assignmentService.getAssignmentByCourseAndStudent(this.course.id,this.principal.getId()).subscribe(assignments => {
        this.assignments = assignments;
        console.log("Assignments: ");
        console.log(assignments);
      })
    }
  }

  private handleDialogResponse(resp): void {
    if (!resp) {
      return;
    } else if (resp.type === 'ADD') {
      console.log("Response from add assignment", resp);

    } else if (resp.type === 'UPDATE') {
      console.log("Response from update assignment", resp);

    } else if (resp.type === 'DELETE') {
      console.log("Response from delete assignment", resp);

    }

    this.ngOnInit();
  }

  private getAssignment(row): any {
    if(this.studentView){
      return row.assignment
    } else {
      return row
    }
  }

  stopPropagation(e): void {
    e.stopPropagation();
  }

}
