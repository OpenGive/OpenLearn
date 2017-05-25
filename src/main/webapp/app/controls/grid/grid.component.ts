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

  displayCell(row, column) {
    if (column.property === 'authorities') {
      return '[authorities]';
    } else if (column.property === 'imageUrl') {
      return '[image]';
    } else {
      return row[column.property];
    }
  }

}
