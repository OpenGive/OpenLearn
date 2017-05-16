import {Component} from "@angular/core";
import {MdDialogRef} from "@angular/material";

@Component({
  selector: 'forgot-password-dialog',
  templateUrl: './forgot-password-dialog.component.html',
  styleUrls: ['./forgot-password-dialog.component.css']
})
export class ForgotPasswordDialogComponent {
  constructor(public dialogRef: MdDialogRef<ForgotPasswordDialogComponent>) {}
}
