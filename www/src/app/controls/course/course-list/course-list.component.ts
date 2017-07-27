import {Component, Input} from '@angular/core';

import {Course} from '../../../models/course';

@Component({
  selector: 'app-course-list',
  templateUrl: './course-list.component.html',
  styleUrls: ['./course-list.component.css']
})
export class CourseListComponent {

  @Input() courses: Course[] = [];
  @Input() studentView: boolean = false;
}
