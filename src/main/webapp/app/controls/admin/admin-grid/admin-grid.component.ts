import {Component, Input} from "@angular/core";
import {AdminGridModel} from "../../../models/admin-grid.model";

@Component({
  selector: 'app-admin-grid',
  templateUrl: './admin-grid.component.html',
  styleUrls: ['./admin-grid.component.css']
})
export class AdminGridComponent {

  @Input() grid: AdminGridModel;

  displayCell(row, column) {
    if (column.property === 'authorities') {
      // Parse array of roles to create friendly list (ROLE_BLAH => Blah)
      return row[column.property].map(role => {
        return role.charAt(5) + role.slice(6).toLowerCase()
      }).reverse().join(', ');
    } else {
      return row[column.property];
    }
  }

}
