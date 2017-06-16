import { Milestone } from './../../../models/milestone';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-course-milestone-list',
  templateUrl: './course-milestone-list.component.html',
  styleUrls: ['./course-milestone-list.component.css']
})
export class CourseMilestoneListComponent implements OnInit {

  constructor() { }
  @Input() milestones: Milestone[] = [];
  ngOnInit() {
  }

}
