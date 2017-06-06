import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-sessions-form',
  templateUrl: './admin-sessions-form.component.html',
  styleUrls: ['./admin-sessions-form.component.css', '../../admin-forms.css']
})
export class AdminSessionsFormComponent implements OnInit {

  @Input('item') formSession: any;

  constructor(public dialogRef: MdDialogRef<AdminSessionsFormComponent>) {}

  ngOnInit() {
    console.log(this.formSession);
  }
}
