import {Component} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {AppConstants} from  "../../app.constants";

@Component({
  selector: 'forgot-password-dialog',
  templateUrl: './forgot-password-dialog.component.html',
  styleUrls: ['./forgot-password-dialog.component.css']
})
export class ForgotPasswordDialogComponent {
  public forgotPasswordEmail : string;
  constructor(public dialogRef: MdDialogRef<ForgotPasswordDialogComponent>) {
    this.forgotPasswordEmail = AppConstants.ForgotPasswordEmail;
  }
}
