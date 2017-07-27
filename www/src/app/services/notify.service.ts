import {Injectable} from "@angular/core";
import {MdSnackBar} from "@angular/material";

@Injectable()
export class NotifyService {

  private duration = 3000; // milliseconds to leave notification open

  private actionText = 'OK'; // text to appear on the right

  private styles = { // css classes affecting each message type
    success: ['success-snack'],
    warning: ['warning-snack'],
    error: ['error-snack']
  };

  constructor(private snackBar: MdSnackBar) {}

  success(message: string, actionText = this.actionText, callback = () => {}): void {
    this.snackBar.open(message, actionText, {
        duration: this.duration,
        extraClasses: this.styles.success
      }).onAction().subscribe(() => callback());
  }

  warning(message: string, actionText = this.actionText, callback = () => {}): void {
    this.snackBar.open(message, actionText, {
        duration: this.duration,
        extraClasses: this.styles.warning
      }).onAction().subscribe(() => callback());
  }

  error(message: string, actionText = this.actionText, callback = () => {}): void {
    this.snackBar.open(message, actionText, {
        duration: this.duration,
        extraClasses: this.styles.error
      }).onAction().subscribe(() => callback());
  }

}
