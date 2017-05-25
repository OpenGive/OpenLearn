import {Component} from "@angular/core";
import {MdDialogRef} from "@angular/material";

@Component({
  selector: 'admin-users-dialog',
  templateUrl: './admin-users-dialog.component.html',
  styleUrls: ['./admin-users-dialog.component.css']
})
export class AdminUsersDialogComponent {
  constructor(public dialogRef: MdDialogRef<AdminUsersDialogComponent>) {}
}
