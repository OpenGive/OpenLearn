import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {AdminService} from "../../../../../services/admin.service";
import {AdminModel} from "../../../admin.constants";

@Component({
  selector: 'admin-programs-form',
  templateUrl: './admin-programs-form.component.html',
  styleUrls: ['./admin-programs-form.component.css', '../../admin-forms.css']
})
export class AdminProgramsFormComponent implements OnInit {

  @Input('item') formProgram: any;
  @Input() editing: boolean;

  schools: any[];
  sessions: any[];

  constructor(public dialogRef: MdDialogRef<AdminProgramsFormComponent>,
              private adminService: AdminService) {}

  ngOnInit() {
    this.getSchools();
    this.getSessions();
  }

  getSchools(): void {
    this.adminService.getAll(AdminModel.School.route).subscribe(resp => this.schools = resp);
  }

  getSessions(): void {
    this.adminService.getAll(AdminModel.Session.route).subscribe(resp => this.sessions = resp);
  }

  displaySchool(school): string {
    return school ? school.name : '';
  }

  displaySession(session): string {
    return session ? session.name : '';
  }
}
