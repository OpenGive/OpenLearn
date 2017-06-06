import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {Role} from "../../../../../app.constants";

@Component({
  selector: 'admin-organizations-form',
  templateUrl: './admin-organizations-form.component.html',
  styleUrls: ['./admin-organizations-form.component.css', '../../admin-forms.css']
})
export class AdminOrganizationsFormComponent implements OnInit {

  @Input('item') formOrganization: any;

  constructor(public dialogRef: MdDialogRef<AdminOrganizationsFormComponent>) {}

  ngOnInit() {
    console.log(this.formOrganization);
  }
}
