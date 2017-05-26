import {Component, Input, OnInit} from "@angular/core";
import {AdminGridModel} from "../../../models/admin-grid.model";
import {AdminDialogComponent} from "../admin-dialog/admin-dialog.component";
import {MdDialog} from "@angular/material";
import {AdminGridService} from "../../../services/admin-grid.service";

@Component({
  selector: 'app-admin-grid',
  templateUrl: './admin-grid.component.html',
  styleUrls: ['./admin-grid.component.css']
})
export class AdminGridComponent implements OnInit {

  @Input() grid: AdminGridModel;

  constructor(private dialog: MdDialog,
              private adminGridService: AdminGridService) {}

  ngOnInit(): void {
    this.getRows();
  }

  getRows(): void {
    this.adminGridService.query(this.grid.title).subscribe(resp => this.grid.rows = resp);
  }

  add(): void {
    console.log('add');
    this.dialog.open(AdminDialogComponent);
  }

  edit(): void {
    console.log('edit');
    this.dialog.open(AdminDialogComponent);
  }

  remove(): void {
    console.log('remove');
  }

  viewDetails(): void {
    console.log('viewDetails');
  }

  displayCell(row, column): string {
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
