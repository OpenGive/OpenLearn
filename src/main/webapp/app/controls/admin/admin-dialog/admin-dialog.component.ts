import {Component, Inject, OnInit} from "@angular/core";
import {MdDialogRef, MD_DIALOG_DATA} from "@angular/material";

@Component({
  selector: 'admin-dialog',
  templateUrl: './admin-dialog.component.html',
  styleUrls: ['./admin-dialog.component.css']
})
export class AdminDialogComponent implements OnInit {

  constructor(@Inject(MD_DIALOG_DATA) public data: any,
              public dialogRef: MdDialogRef<AdminDialogComponent>) {}

  ngOnInit() {
  }

  discard() {
    this.dialogRef.close();
  }

  save() {
    this.dialogRef.close();
  }
}
