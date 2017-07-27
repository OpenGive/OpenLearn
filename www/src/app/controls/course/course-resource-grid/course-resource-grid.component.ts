import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import * as _ from "lodash";

import {Course} from '../../../models/course';
import {CourseService} from "../../../services/course.service";
import {ResourceDialogComponent} from "../resource-dialog.component"

@Component({
  selector: 'app-course-resource-grid',
  templateUrl: './course-resource-grid.component.html',
  styleUrls: ['../course-grid/course-grid.component.css']
})
export class CourseResourceGridComponent implements OnInit {

  @Input() course: Course;
  @Input() resource: any;
  resources: any[];
  columns: any[]

  sortColumn: any;
  reverse: boolean;

  constructor(private dialog: MdDialog,
              private courseService: CourseService) {}

  ngOnInit(): void {

    this.columns = [
      {
        id: "resource.name",
        name: "Name"
      },
      {
        id: "resource.description",
        name: "Description"
      },
      {
        id: "resource.thumbnailImageUrl",
        name: "Thumbnail image"
      },
      {
        id: "resource.itemUrl",
        name: "URL"
      }
    ];

    this.getCourseResources();

  }

  add(): void {
    this.dialog.open(ResourceDialogComponent, {
      data: {
        course: this.course
      },
      width: "400px",
      height: "600px",
      disableClose: true
    }).afterClosed().subscribe(resp => {
      this.handleAddResourceResponse(resp)
    });
  }


  removeResource(resourceId: Number): void {
    this.courseService.removeResourceFromCourse(this.course.id, resourceId).subscribe(resources => {
      this.resources = this.resources.filter(resources => resources.id !== resourceId);
    })
  }

  getCourseResources(): void {
    this.courseService.getCourseResources(this.course.id).subscribe(resources => {
      this.resources = resources;
    })
  }

  private handleAddResourceResponse(resp): void {
    if (resp) {
      console.log("Response from add resource", resp);

      var resourceData = resp.data;

      this.resources.push(resourceData);
    }
  }

}
