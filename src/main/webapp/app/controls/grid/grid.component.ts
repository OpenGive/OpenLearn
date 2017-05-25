import {Component, Input, OnInit} from "@angular/core";
import {GridModel} from "../../models/grid.model";

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent implements OnInit {

  @Input() grid: GridModel;

  constructor() {
  }

  ngOnInit() {
  }

}
