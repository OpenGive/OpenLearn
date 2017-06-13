import { Activity } from './../../../models/activity';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-course-activity-list',
  templateUrl: './course-activity-list.component.html',
  styleUrls: ['./course-activity-list.component.css']
})
export class CourseActivityListComponent implements OnInit {

  constructor() { }
  @Input() activities: Activity[] = [];
  ngOnInit() {
  }

}
