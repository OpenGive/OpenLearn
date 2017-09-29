import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";

import {Course} from '../../../models/course.model';
import {StudentCourseService} from "../../../services/student-course.service";
import {AssignmentService} from "../../../services/assignment.service";

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
              private assignmentService: AssignmentService) {}

  ngOnInit(): void {

    this.columns = [
      {
        id: "resource.name",
        name: "Name"
      },
      {
        id: "resource.description",
        name: "Description"
      }
    ];

    this.getCourseAssignments();

  }

  add(): void {
    console.log('added');
    // TODO: No longer using ResourceDialogComponent
    // this.dialog.open(ResourceDialogComponent, {
    //   data: {
    //     course: this.course
    //   },
    //   width: "400px",
    //   height: "600px",
    //   disableClose: true
    // }).afterClosed().subscribe(resp => {
    //   this.handleAddResourceResponse(resp)
    // });
  }


  // removeResource(resourceId: Number): void {
  //   this.courseService.removeResourceFromCourse(this.course.id, resourceId).subscribe(resources => {
  //     this.resources = this.resources.filter(resources => resources.id !== resourceId);
  //   })
  // }

  getCourseAssignments(): void {
    this.assignmentService.getAssignmentsByCourse(this.course.id).subscribe( assignments => {
      this.assignments = assignments;
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
