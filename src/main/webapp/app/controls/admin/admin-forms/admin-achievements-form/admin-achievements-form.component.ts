import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AdminModel} from "../../admin.constants";
import {AdminService} from "../../../../services/admin.service";
import {NotifyService} from "../../../../services/notify.service";
import {UserService} from "../../../../services/user.service";

@Component({
  selector: 'admin-achievements-form',
  templateUrl: './admin-achievements-form.component.html',
  styleUrls: ['../admin-forms.css']
})
export class AdminAchievementsFormComponent implements OnInit {

  @Input('item') formAchievement: any;
  @Input() adding: boolean;
  editing: boolean;

  milestones: any[];
  students: any[];

  achievementForm: FormGroup;
  formErrors = {
    name: '',
    description: '',
    badgeUrl: '',
    milestone: '',
    achievedBy: ''
  };
  validationMessages = {
    name: {
      required: 'Name is required',
      minlength: 'Name must be at least 3 characters long',
      maxlength: 'Name cannot be more than 100 characters long'
    },
    description: {
      minlength: 'Description must be at least 10 characters long',
      maxlength: 'Description cannot be more than 200 characters long'
    },
    badgeUrl: {
      minlength: 'Badge URL must be at least 10 characters long',
      maxlength: 'Badge URL cannot be more than 200 characters long'
    },
    milestone: {
      required: 'Milestone is required' // TODO: Seems like this should be the case
    },
    achievedBy: {
      required: 'Achieved By is required'
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
    this.getStudents();
    this.getMilestones();
  }

  private buildForm(): void {
    this.achievementForm = this.fb.group({
      name: [this.formAchievement.name, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(100)
      ]],
      description: [this.formAchievement.description, [
        Validators.minLength(10),
        Validators.maxLength(200)
      ]],
      badgeUrl: [this.formAchievement.badgeUrl, [
        Validators.minLength(10),
        Validators.maxLength(200)
      ]],
      milestone: [this.formAchievement.milestone, [
        Validators.required
      ]],
      achievedBy: [this.formAchievement.achievedBy, [
        Validators.required
      ]]
    });
    this.achievementForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.achievementForm) {
      const form = this.achievementForm;
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
    if (this.achievementForm) {
      if (editing) {
        this.achievementForm.enable();
        this.editing = true;
      } else {
        this.achievementForm.disable();
        this.editing = false;
      }
    }
  }

  private getStudents(): void {
    this.userService.getStudents().subscribe(resp => {
      this.students = resp;
    });
  }

  private getMilestones(): void {
    this.adminService.getAll(AdminModel.Milestone.route).subscribe(resp => {
      this.milestones = resp;
    });
  }

  save(): void {
    if (this.achievementForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.achievementForm.markAsTouched();
    }
  }

  private add(): void {
    this.adminService.create(AdminModel.Achievement.route, this.achievementForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added achievement');
    }, error => {
      this.notify.error('Failed to add achievement');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminModel.Achievement.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated achievement');
    }, error => {
      this.notify.error('Failed to update achievement');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formAchievement.id,
      name: this.achievementForm.get('name').value,
      description: this.achievementForm.get('description').value,
      badgeUrl: this.achievementForm.get('badgeUrl').value,
      milestone: this.achievementForm.get('milestone').value,
      achievedBy: this.achievementForm.get('achievedBy').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminModel.Achievement.route, this.formAchievement.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formAchievement.id
        }
      });
      this.notify.success('Successfully deleted achievement');
    }, error => {
      this.notify.error('Failed to delete achievement');
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

  displayMilestone(milestone: any): string {
    return milestone ? milestone.name : '';
  }

  displayStudent(student: any): string {
    return student ? student.lastName + ', ' + student.firstName : '';
  }
}
