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

  edit() {
    this.data.item.editing = true;
  }

  discard() {
    this.data.item.editing = false;
  }

  save() {
    this.data.item.editing = false;
  }
}
