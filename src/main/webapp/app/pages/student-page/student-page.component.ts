import { Course } from './../../models/course';
import { CourseService } from './../../services/course.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-student-page',
  templateUrl: './student-page.component.html',
  styleUrls: ['./student-page.component.css']
})
export class StudentPageComponent implements OnInit {

  
  leftNavIsOpen: boolean = true;
  
  
  ngOnInit() {
   
  }
  
  
}
