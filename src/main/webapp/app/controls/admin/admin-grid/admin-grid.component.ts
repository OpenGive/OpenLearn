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
    this.dialog.open(AdminDialogComponent);
  }

  edit(row): void {
    this.dialog.open(AdminDialogComponent, {
      data: {
        tab: this.grid.title,
        item: row
      },
      disableClose: true
    });
  }

  remove(row): void {
  }

  viewDetails(row): void {
  }

  displayCell(row, column): string {
    if (column.property === 'authorities') {
      // Parse array of roles to create friendly list (ROLE_BLAH => Blah)
      return row[column.property].map(role => {
        return role.charAt(5) + role.slice(6).toLowerCase()
      }).sort().join(', ');
    } else {
      return row[column.property];
    }
  }

}
