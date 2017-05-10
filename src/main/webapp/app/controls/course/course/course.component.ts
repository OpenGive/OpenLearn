import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  myPets = [
    {name: "charles", breed: "Lab"},
    {name: "gggg", breed: "Lab"},
    {name: "hhhh", breed: "Lab"},
    {name: "jjjjj", breed: "Lab"}
  ]
}
