import {Component, Input, OnInit} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {AdminModel} from "../../../admin.constants";
import {AdminService} from "../../../../../services/admin.service";
import {UserService} from "../../../../../services/user.service";

@Component({
  selector: 'admin-courses-form',
  templateUrl: './admin-courses-form.component.html',
  styleUrls: ['./admin-courses-form.component.css', '../../admin-forms.css']
})
export class AdminCoursesFormComponent implements OnInit {

  @Input('item') formCourse: any;
  @Input() editing: boolean;

  instructors: any[];
  organizations: any[];
  programs: any[];

  constructor(public dialogRef: MdDialogRef<AdminCoursesFormComponent>,
              private adminService: AdminService,
              private userService: UserService) {}

  ngOnInit() {
    this.getInstructors();
    this.getOrganizations();
    this.getPrograms();
  }

  getInstructors(): void {
    this.userService.getInstructors().subscribe(resp => this.instructors = resp);
  }

  getOrganizations(): void {
    this.adminService.getAll(AdminModel.Organization.route).subscribe(resp => this.organizations = resp);
  }

  getPrograms(): void {
    this.adminService.getAll(AdminModel.Program.route).subscribe(resp => this.programs = resp);
  }

  displayInstructor(instructor): string {
    return instructor ? instructor.lastName + ', ' + instructor.firstName : '';
  }

  displayOrganization(organization): string {
    return organization ? organization.name : '';
  }

  displayProgram(program): string {
    return program ? program.name : '';
  }
}
