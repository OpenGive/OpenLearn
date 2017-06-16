import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import * as _ from "lodash";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AppConstants} from "../../../../app.constants";
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

  roles: string[];
  states: any[];

  achievementForm: FormGroup;
  formErrors = {
    name: '',
    description: '',
    activity: '',
    achievedBy: {
      firstName: '',
      lastName: '',
    }
  };
  validationMessages = {
    name: {
      maxlength: 'Achievement name cannot be more than 50 characters long'
    },
    description: {
      maxlength: 'Username cannot be more than 100 characters long'
    },
    activity: {
      required: 'Activity is required'
    },
    achievedBy: {
      firstName: {
        maxLength: 'First Name cannot be more than 50 characters long'
      },
      lastName: {
        maxLength: 'Last Name cannot be more than 50 characters long'
      }
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
    private fb: FormBuilder,
    private userService: UserService,
    private notify: NotifyService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.getRoles();
    this.getStates();
  }

  private buildForm(): void {
    this.achievementForm = this.fb.group({
      name: [this.formAchievement.name, [
      Validators.maxLength(50)
      ]],
      description: [this.formAchievement.description, [
      Validators.maxLength(50)
      ]],
      activity: [this.formAchievement.activity, [
      Validators.required
      ]],
      achievedBy: this.fb.group({
        firstName: [this.formAchievement.firstName, [
        Validators.maxLength(50)
        ]],
        lastName: [this.formAchievement.lastName, [
        Validators.maxLength(50)
        ]]
      })
    });
    this.achievementForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.achievementForm) {
      const form = this.achievementForm;
      this.updateFormErrors(form, this.formErrors, this.validationMessages);
    }
  }

  // Recursive method to account for nested FormGroups
  private updateFormErrors(form: FormGroup, formErrors: any, validationMessages: any): void {
    for (const field in formErrors) {
      const control = form.get(field);
      if (control instanceof FormGroup) {
        this.updateFormErrors(control, formErrors[field], validationMessages[field]);
      } else {
        formErrors[field] = '';
        if (control && control.dirty && !control.valid) {
          const messages = validationMessages[field];
          for (const key in control.errors) {
            formErrors[field] += messages[key] + ' ';
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

  getRoles(): void {
    this.roles = Object.keys(AppConstants.Role).map(key => AppConstants.Role[key]);
  }

  getStates(): void {
    this.states = AppConstants.States;
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
    console.log(this.achievementForm.value);
    this.userService.create(this.achievementForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added instructor');
    }, error => {
      this.notify.error('Failed to add instructor');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.userService.update(toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated instructor');
    }, error => {
      this.notify.error('Failed to update instructor');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formAchievement.id,
      firstName: this.achievementForm.get('firstName').value,
      lastName: this.achievementForm.get('lastName').value,
      login: this.achievementForm.get('login').value,
      password: this.achievementForm.get('password').value,
      authorities: this.achievementForm.get('authorities').value,
      biography: this.achievementForm.get('biography').value,
      email: this.achievementForm.get('email').value,
      phoneNumber: this.achievementForm.get('phoneNumber').value,
      address: {
        streetAddress1: this.achievementForm.get('address').get('streetAddress1').value,
        streetAddress2: this.achievementForm.get('address').get('streetAddress2').value,
        city: this.achievementForm.get('address').get('city').value,
        state: this.achievementForm.get('address').get('state').value,
        postalCode: this.achievementForm.get('address').get('postalCode').value
      },
      imageUrl: this.achievementForm.get('imageUrl').value,
      activated: this.achievementForm.get('activated').value,
      is14Plus: this.achievementForm.get('is14Plus').value
    };
  }

  delete(): void {
    this.userService.delete(this.formAchievement.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formAchievement.id
        }
      });
      this.notify.success('Successfully deleted instructor');
    }, error => {
      this.notify.error('Failed to delete instructor');
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

  displayRole(role: string): string { // Convert "ROLE_ONE_TWO" to "One Two"
  return role.split('_').slice(1).map(str => str.charAt(0) + str.slice(1).toLowerCase()).join(' ');
}

displayState(stateCode: string): string {
  return stateCode ? _.filter(AppConstants.States, {code: stateCode})[0].name : '';
}
}
