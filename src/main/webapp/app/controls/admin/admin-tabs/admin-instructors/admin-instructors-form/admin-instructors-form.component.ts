import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-instructors-form',
  templateUrl: './admin-instructors-form.component.html',
  styleUrls: ['./admin-instructors-form.component.css', '../../admin-forms.css']
})
export class AdminInstructorsFormComponent implements OnInit {

  @Input('item') formInstructor: any;
  @Input() editing: boolean;

  roles: string[];

  constructor(public dialogRef: MdDialogRef<AdminInstructorsFormComponent>) {}

  ngOnInit() {
    this.getRoles();
  }

  getRoles(): void {
    this.roles = Object.keys(Role).map(key => Role[key]);
  }
}
