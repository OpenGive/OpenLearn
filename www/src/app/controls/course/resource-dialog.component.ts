import {Component, Input, Inject, OnInit} from "@angular/core";
import {MdDialog, MdDialogRef, MD_DIALOG_DATA} from "@angular/material";
import {FormBuilder, FormsModule, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {StudentCourseService} from "../../services/student-course.service";
import {NotifyService} from "../../services/notify.service";
import {ItemLinkService} from "../../services/itemlink.service";
import {Course} from "../../models/course.model";

@Component({
  selector: 'resource-dialog',
  template: `
    <div class="course-dialog">
      <div class="ol-dialog-header" fxLayout="row" fxLayoutAlign="space-between center">
        <div fxLayout="row" fxLayoutAlign="start center">
          <div><button md-button type="reset" (click)="close()"><i class="fa fa-times fa-lg"></i></button></div>
          <span class="ol-dialog-title">Create and Add Resource</span>
        </div>
      </div>
      <div class="ol-dialog-content course-view-body">
        <div>
          <md-input-container fxFlex="100%">
            <input [(ngModel)]="this.name" mdInput placeholder="Name" type="text" required md-maxlength="64"/>
          </md-input-container>
          <md-input-container fxFlex="100%">
            <input [(ngModel)]="this.description" placeholder="Description" mdInput type="text" required md-maxlength="256"/>
          </md-input-container>
          <md-input-container fxFlex="100%">
            <input [(ngModel)]="this.thumbnailImageUrl" placeholder="Thumbnail Image URL" mdInput type="text" required md-maxlength="256"/>
          </md-input-container>
          <md-input-container fxFlex="100%">
            <input [(ngModel)]="this.itemUrl" placeholder="Resource URL" mdInput type="text" required md-maxlength="256"/>
          </md-input-container>
          <button (click)="save()" md-raised-button type="button" class="ol-dialog-button grey-button" mdTooltip="Save">
            <md-icon>save</md-icon>
          </button>
        </div>
      </div>
    </div>
    `,
  styleUrls: ['./course-grid/course-grid.component.css', '../dialog-forms.css']
})
export class ResourceDialogComponent implements OnInit {

  course: Course;
  name: string;
  description: string;
  thumbnailImageUrl: string;
  itemUrl: string;

  ngOnInit(): void {
    this.course = this.data.course;
  }

  constructor(private dialog: MdDialogRef<ResourceDialogComponent>,
              @Inject(MD_DIALOG_DATA) public data: any,
              private courseService: StudentCourseService,
              private notify: NotifyService,
              private itemLinkService: ItemLinkService) {}

  close(): void {
    this.dialog.close();
  }

  save(): void {
    this.itemLinkService.create(this.name, this.description, this.thumbnailImageUrl, this.itemUrl).subscribe(resp => {
      this.courseService.addResourceToCourse(this.course.id, resp.id).subscribe(resp2 => {
        this.dialog.close( {
          type: 'ADD',
          data: resp
        });
        this.notify.success('Successfully added resource');
      }, error=> {
        this.notify.error('Failed to add resource');
      });
      this.notify.success('Successfully created resource');
    }, error =>{
      this.notify.error('Failed to create resource');
    });
  }

}
