import {Component, Inject} from "@angular/core";
import {MD_DIALOG_DATA} from "@angular/material";

@Component({
  selector: 'admin-dialog',
  template: `
    <div class="admin-dialog" [ngSwitch]="data.tab">
      <admin-organizations-form *ngSwitchCase="'organizations'" [item]="data.item" [adding]="data.adding"></admin-organizations-form>
      <admin-administrators-form *ngSwitchCase="'administrators'" [item]="data.item" [adding]="data.adding"></admin-administrators-form>
      <admin-instructors-form *ngSwitchCase="'instructors'" [item]="data.item" [adding]="data.adding"></admin-instructors-form>
      <admin-students-form *ngSwitchCase="'students'" [item]="data.item" [adding]="data.adding"></admin-students-form>
      <admin-sessions-form *ngSwitchCase="'sessions'" [item]="data.item" [adding]="data.adding"></admin-sessions-form>
      <admin-programs-form *ngSwitchCase="'programs'" [item]="data.item" [adding]="data.adding"></admin-programs-form>
      <admin-courses-form *ngSwitchCase="'courses'" [item]="data.item" [adding]="data.adding"></admin-courses-form>
      <admin-achievements-form *ngSwitchCase="'achievements'" [item]="data.item" [adding]="data.adding"></admin-achievements-form>
    </div>`
})
export class AdminDialogComponent {

  constructor(@Inject(MD_DIALOG_DATA) public data: any) {}
}
