import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {Course} from '../../../models/course.model';
import {StudentCourseService} from "../../../services/student-course.service";
import {AssignmentService} from "../../../services/assignment.service";
import {AssignmentFormComponent} from "../assignment/assignment-form.component";
import {AdminTabs} from "../../admin/admin.constants";
import {AdminService} from "../../../services/admin.service";

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

  constructor(private dialog: MdDialog,
              private courseService: StudentCourseService,
              private assignmentService: AssignmentService,
              private adminService: AdminService) {}

  ngOnInit(): void {

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

    this.getCourseAssignments();
    console.log("Assignment: " + this.assignment);
    console.log("Assignemnts2: " + this.assignments);

  }

  add(): void {
    console.log('added');
    this.dialog.open(AssignmentFormComponent, {
      data: {
        course: this.course,
        adding: true
      },
      width: "400px",
      height: "600px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
      //this.handleAddResourceResponse(resp)
    });
  }

  removeAssignment(assignmentId: Number): void {
    this.adminService.delete(AdminTabs.Assignment.route,assignmentId).subscribe(resp=> {
      this.assignments = _.filter(this.assignments, assignment => assignment.id !== assignmentId)
    })
  }


  // removeResource(resourceId: Number): void {
  //   this.courseService.removeResourceFromCourse(this.course.id, resourceId).subscribe(resources => {
  //     this.resources = this.resources.filter(resources => resources.id !== resourceId);
  //   })
  // }

  getCourseAssignments(): void {
    this.assignmentService.getAssignmentsByCourse(this.course.id).subscribe( assignments => {
      this.assignments = assignments;
      console.log("Assignemnts1: " + this.assignments);
    })
  }

  // private handleAddResourceResponse(resp): void {
  //   if (resp) {
  //     console.log("Response from add resource", resp);
  //
  //     var resourceData = resp.data;
  //
  //     this.resources.push(resourceData);
  //   }
  // }

}
