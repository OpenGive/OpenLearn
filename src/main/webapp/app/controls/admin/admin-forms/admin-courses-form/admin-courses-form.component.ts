import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AdminModel} from "../../admin.constants";
import {AdminService} from "../../../../services/admin.service";
import {NotifyService} from "../../../../services/notify.service";
import {UserService} from "../../../../services/user.service";

@Component({
  selector: 'admin-courses-form',
  templateUrl: './admin-courses-form.component.html',
  styleUrls: ['../admin-forms.css']
})
export class AdminCoursesFormComponent implements OnInit {

  @Input('item') formCourse: any;
  @Input() adding: boolean;
  editing: boolean;

  instructors: any[];
  organizations: any[];
  programs: any[];

  courseForm: FormGroup;
  formErrors = {
    name: '',
    description: '',
    organization: '',
    program: '',
    instructor: '',
    startDate: '',
    endDate: ''
  };
  validationMessages = {
    name: {
      required: 'Name is required',
      minlength: 'Name must be at least 3 characters long',
      maxlength: 'Name cannot be more than 100 characters long'
    },
    description: {
      minlength: 'Description must be at least 5 characters long',
      maxlength: 'Description cannot be more than 200 characters long'
    },
    organization: {
      required: 'Organization is required'
    },
    program: {
      required: 'Program is required'
    },
    instructor: {
      required: 'Instructor is required'
    },
    startDate: {
      mdDatepickerMax: 'Start date must not be after End Date'
    },
    endDate: {
      mdDatepickerMin: 'End date must not be before Start Date'
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              private adminService: AdminService,
              private userService: UserService,
              private notify: NotifyService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.getInstructors();
    this.getOrganizations();
    this.getPrograms();
  }

  private buildForm(): void {
    this.courseForm = this.fb.group({
      name: [this.formCourse.name, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(100)
      ]],
      description: [this.formCourse.description, [
        Validators.minLength(5),
        Validators.maxLength(200)
      ]],
      organization: [this.formCourse.organization, [
        Validators.required
      ]],
      program: [this.formCourse.program, [
        Validators.required
      ]],
      instructor: [this.formCourse.instructor, [
        Validators.required
      ]],
      startDate: [this.formCourse.startDate],
      endDate: [this.formCourse.endDate]
    });
    this.courseForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.courseForm) {
      const form = this.courseForm;
      for (const field in this.formErrors) {
        this.formErrors[field] = '';
        const control = form.get(field);
        if (control && control.dirty && !control.valid) {
          const messages = this.validationMessages[field];
          for (const key in control.errors) {
            this.formErrors[field] += messages[key] + ' ';
          }
        }
      }
    }
  }

  private setEditing(editing: boolean): void {
    if (this.courseForm) {
      if (editing) {
        this.courseForm.enable();
        this.editing = true;
      } else {
        this.courseForm.disable();
        this.editing = false;
      }
    }
  }

  private getInstructors(): void {
    this.userService.getInstructors().subscribe(resp => {
      this.instructors = resp;
    });
  }

  private getOrganizations(): void {
    this.adminService.getAll(AdminModel.Organization.route).subscribe(resp => {
      this.organizations = resp;
    });
  }

  private getPrograms(): void {
    this.adminService.getAll(AdminModel.Program.route).subscribe(resp => {
      this.programs = resp;
    });
  }

  save(): void {
    if (this.courseForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.courseForm.markAsTouched();
    }
  }

  private add(): void {
    this.adminService.create(AdminModel.Course.route, this.courseForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added course');
    }, error => {
      this.notify.error('Failed to add course');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminModel.Course.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated course');
    }, error => {
      this.notify.error('Failed to update course');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formCourse.id,
      name: this.courseForm.get('name').value,
      description: this.courseForm.get('description').value,
      organization: this.courseForm.get('organization').value,
      program: this.courseForm.get('program').value,
      instructor: this.courseForm.get('instructor').value,
      startDate: this.courseForm.get('startDate').value,
      endDate: this.courseForm.get('endDate').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminModel.Course.route, this.formCourse.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formCourse.id
        }
      });
      this.notify.success('Successfully deleted course');
    }, error => {
      this.notify.error('Failed to delete course');
    });
  }

  edit(): void {
    this.setEditing(true);
  }

  cancel(): void {
    this.ngOnInit();
  }

  close(): void {
    this.dialogRef.close();
  }

  displayInstructor(instructor: any): string {
    return instructor ? instructor.lastName + ', ' + instructor.firstName : '';
  }

  displayOrganization(organization: any): string {
    return organization ? organization.name : '';
  }

  displayProgram(program: any): string {
    return program ? program.name : '';
  }
}
