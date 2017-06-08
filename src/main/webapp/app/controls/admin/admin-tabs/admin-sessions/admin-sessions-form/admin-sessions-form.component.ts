import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {AdminService} from "../../../../../services/admin.service";
import {AdminModel} from "../../../admin.constants";

@Component({
  selector: 'admin-sessions-form',
  templateUrl: './admin-sessions-form.component.html',
  styleUrls: ['./admin-sessions-form.component.css', '../../admin-forms.css']
})
export class AdminSessionsFormComponent implements OnInit {

  @Input('item') formSession: any;
  @Input() editing: boolean;

  organizations: any[];

  constructor(public dialogRef: MdDialogRef<AdminSessionsFormComponent>,
              private adminService: AdminService) {}

  ngOnInit() {
    this.getOrganizations();
  }

  getOrganizations(): void {
    this.adminService.getAll(AdminModel.Organization.route).subscribe(resp => this.organizations = resp);
  }

  displayOrganization(organization): string {
    return organization ? organization.name : '';
  }
}
