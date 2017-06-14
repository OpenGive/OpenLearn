import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";

import {AdminDialogComponent} from "../../../admin-dialog.component";
import {AppConstants} from "../../../../../app.constants";
import {UserService} from "../../../../../services/user.service";

@Component({
  selector: 'admin-administrators-form',
  templateUrl: './admin-administrators-form.component.html',
  styleUrls: ['../../admin-forms.css']
})
export class AdminAdministratorsFormComponent implements OnInit {

  @Input('item') formAdministrator: any;
  @Input() adding: boolean;
  editing: boolean;

  roles: string[];

  administratorForm: FormGroup;
  formErrors = {
    firstName: '',
    lastName: '',
    login: '',
    password: '',
    authorities: '',
    biography: '',
    email: '',
    streetAddress1: '',
    streetAddress2: '',
    city: '',
    postalCode: ''
  };
  validationMessages = {
    firstName: {
      maxlength: 'First Name cannot be more than 50 characters long'
    },
    lastName: {
      maxlength: 'Last Name cannot be more than 50 characters long'
    },
    login: {
      required: 'Username is required',
      pattern: 'Username contains invalid characters',
      maxlength: 'Username cannot be more than 100 characters long'
    },
    password: {
      required: 'Password is required'
    },
    authorities: {
      required: 'Administrator must have at least 1 role'
    },
    biography: {
      maxlength: 'Biography cannot be more than 2000 characters long'
    },
    email: {
      email: 'Email is not formatted correctly',
      minlength: 'Email must be at least 5 characters long',
      maxlength: 'Email cannot be more than 100 characters long'
    },
    phoneNumber: {
      pattern: 'Phone is not formatted correctly',
      maxlength: 'Phone cannot be more than 15 characters long'
    },
    address: {
      streetAddress1: {
        minlength: 'Street Address 1 must be at least 5 characters long',
        maxlength: 'Street Address 1 cannot be more than 50 characters long'
      },
      streetAddress2: {
        minlength: 'Street Address 2 must be at least 5 characters long',
        maxlength: 'Street Address 2 cannot be more than 50 characters long'
      },
      city: {
        minlength: 'City must be at least 5 characters long',
        maxlength: 'City cannot be more than 50 characters long'
      },
      postalCode: {
        pattern: 'Postal Code is not formatted correctly'
      }
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              private userService: UserService) {}

  ngOnInit(): void {
    console.log(this.formAdministrator);
    this.buildForm();
    this.setEditing(this.adding);
    this.getRoles();
  }

  private buildForm(): void {
    this.administratorForm = this.fb.group({
      firstName: [this.formAdministrator.firstName, [
        Validators.maxLength(50)
      ]],
      lastName: [this.formAdministrator.lastName, [
        Validators.maxLength(50)
      ]],
      login: [this.formAdministrator.login, [
        Validators.required,
        Validators.pattern("^[_'.@A-Za-z0-9-]*$"),
        Validators.maxLength(50)
      ]],
      password: [this.formAdministrator.password, this.adding ? [
        Validators.required
      ] : []],
      authorities: [this.formAdministrator.authorities, [
        Validators.required
      ]],
      biography: [this.formAdministrator.biography, [
        Validators.maxLength(2000)
      ]],
      email: [this.formAdministrator.email, [
        Validators.email,
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      phoneNumber: [this.formAdministrator.phoneNumber, [
        // TODO: Pattern
        Validators.maxLength(15)
      ]],
      address: this.fb.group({
        streetAddress1: [this.formAdministrator.streetAddress1, [
          Validators.minLength(5),
          Validators.maxLength(50)
        ]],
        streetAddress2: [this.formAdministrator.streetAddress2, [
          Validators.minLength(5),
          Validators.maxLength(50)
        ]],
        city: [this.formAdministrator.city, [
          Validators.minLength(5),
          Validators.maxLength(50)
        ]],
        state: [this.formAdministrator.state],
        postalCode: [this.formAdministrator.postalCode, [
          // TODO: Pattern
        ]]
      }),
      imageUrl: [this.formAdministrator.imageUrl],
      activated: [this.formAdministrator.activated || false],
      is14Plus: [this.formAdministrator.is14Plus || false]
    });
    this.administratorForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.administratorForm) {
      const form = this.administratorForm;
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
    if (this.administratorForm) {
      if (editing) {
        this.administratorForm.enable();
        this.editing = true;
      } else {
        this.administratorForm.disable();
        this.editing = false;
      }
    }
  }

  save(): void {
    if (this.administratorForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.administratorForm.markAsTouched();
    }
  }

  private add(): void {
    this.userService.create(this.administratorForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.userService.update(toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formAdministrator.id,
      firstName: this.administratorForm.get('firstName').value,
      lastName: this.administratorForm.get('lastName').value,
      login: this.administratorForm.get('login').value,
      password: this.administratorForm.get('password').value,
      authorities: this.administratorForm.get('authorities').value,
      biography: this.administratorForm.get('biography').value,
      email: this.administratorForm.get('email').value,
      phoneNumber: this.administratorForm.get('phoneNumber').value,
      address: {
        streetAddress1: this.administratorForm.get('address').get('streetAddress1').value,
        streetAddress2: this.administratorForm.get('address').get('streetAddress2').value,
        city: this.administratorForm.get('address').get('city').value,
        state: this.administratorForm.get('address').get('state').value,
        postalCode: this.administratorForm.get('address').get('postalCode').value
      },
      imageUrl: this.administratorForm.get('imageUrl').value,
      activated: this.administratorForm.get('activated').value,
      is14Plus: this.administratorForm.get('is14Plus').value
    };
  }

  delete(): void {
    this.userService.delete(this.formAdministrator.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formAdministrator.id
        }
      });
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

  getRoles(): void {
    this.roles = Object.keys(AppConstants.Role).map(key => AppConstants.Role[key]);
  }
}
