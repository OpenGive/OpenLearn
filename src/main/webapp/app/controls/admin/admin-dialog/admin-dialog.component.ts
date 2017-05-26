import {Component} from "@angular/core";
import {MdDialogRef} from "@angular/material";

@Component({
  selector: 'admin-dialog',
  templateUrl: './admin-dialog.component.html',
  styleUrls: ['./admin-dialog.component.css']
})
export class AdminDialogComponent {
  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>) {}

  discard() {
    console.log('discard');
    this.dialogRef.close();
  }

  save() {
    console.log('save');
    this.dialogRef.close();
  }
}
