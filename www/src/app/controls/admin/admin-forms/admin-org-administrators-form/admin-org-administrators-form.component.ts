import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import * as _ from "lodash";

import {AdminService} from "../../../../services/admin.service";
import {AppConstants} from "../../../../app.constants";
import {NotifyService} from "../../../../services/notify.service";
import {AdminTabs} from "../../admin.constants";
import {Account} from "../../../../models/account.model";
import {ValidationErrors} from "@angular/forms/src/directives/validators";

@Component({
  selector: 'admin-org-administrators-form',
  templateUrl: './admin-org-administrators-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminOrgAdministratorsFormComponent implements OnInit {

  @Input('item') formOrgAdministrator: any;
  @Input() adding: boolean;
  @Input('organizations') organizations: any[];
  @Input('parent') orgAdministratorForm: FormGroup;

  @Output() onAdd = new EventEmitter<Account>();
  @Output() onUpdate = new EventEmitter<Account>();
  @Output() onDelete = new EventEmitter();
  @Output() onEdit = new EventEmitter<boolean>();

  editing: boolean;
  changingPassword: boolean;

  roles: string[];
  states: any[];

  filteredStates: Observable<any[]>;

  formErrors = {
    firstName: '',
    lastName: '',
    login: '',
    password: '',
    notes: '',
    email: '',
    phoneNumber: '',
    organizationId: '',
    streetAddress1: '',
    streetAddress2: '',
    city: '',
    postalCode: '',
    orgRole: ''
  };
  validationMessages = {
    firstName: {
      required: 'First Name is required',
      maxlength: 'First Name cannot be more than 50 characters long'
    },
    lastName: {
      required: 'Last Name is required',
      maxlength: 'Last Name cannot be more than 50 characters long'
    },
    login: {
      required: 'Username is required',
      pattern: 'Username contains invalid characters',
      maxlength: 'Username cannot be more than 100 characters long'
    },
    password: {
      required: 'Password is required',
      pattern: 'Password must be between 8 and 100 characters and contain at least one letter, one digit, and one of !@#$%^&*()_+'
    },
    notes: {
      maxlength: 'Notes cannot be more than 2000 characters long'
    },
    email: {
      // email: 'Email is not formatted correctly', TODO: See comment in buildForm()
      pattern: 'Email is not formatted correctly',
      minlength: 'Email must be at least 5 characters long',
      maxlength: 'Email cannot be more than 100 characters long'
    },
    phoneNumber: {
      pattern: 'Phone is not formatted correctly',
      maxlength: 'Phone cannot be more than 15 characters long'
    },
    organizationId: {
      required: 'Organization is required'
    },
    streetAddress1: {
      minlength: 'Street Address 1 must be at least 5 characters long',
      maxlength: 'Street Address 1 cannot be more than 50 characters long'
    },
    streetAddress2: {
      minlength: 'Street Address 2 must be at least 5 characters long',
      maxlength: 'Street Address 2 cannot be more than 50 characters long'
    },
    city: {
      maxlength: 'City cannot be more than 50 characters long'
    },
    postalCode: {
      pattern: 'Postal Code is not formatted correctly'
    },
    orgRole: {
      required: 'Org Role is required'
    }
  };

  constructor(private fb: FormBuilder,
              private notify: NotifyService,
              private adminService: AdminService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.resetPassword(false);
    this.getRoles();
    this.getStates();
  }

  changingPasswordValidators(control: AbstractControl): ValidationErrors {
    if (this.changingPassword) {
      const validators = Validators.compose([
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Password)
      ]);
      return validators(control);
    } else {
      return null;
    }
  }

  private buildForm(): void {
    const childForm = this.fb.group({
      firstName: [this.formOrgAdministrator.firstName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      lastName: [this.formOrgAdministrator.lastName, [
        Validators.required,
        Validators.maxLength(50)
      ]],
      login: [this.formOrgAdministrator.login, [
        Validators.required,
        Validators.pattern(AppConstants.OLValidators.Login),
        Validators.maxLength(50)
      ]],
      password: [this.formOrgAdministrator.password, [
        this.changingPasswordValidators.bind(this)
      ]],
      authority: [AppConstants.Role.OrgAdmin.name],
      organizationId: [this.formOrgAdministrator.organizationId, [
        Validators.required
      ]],
      notes: [this.formOrgAdministrator.notes, [
        Validators.maxLength(2000)
      ]],
      email: [this.formOrgAdministrator.email, [
        // Validators.email, TODO: This forces email to be required, https://github.com/angular/angular/pull/16902 is the fix, pattern below is the workaround
        Validators.pattern(AppConstants.OLValidators.Email),
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      phoneNumber: [this.formOrgAdministrator.phoneNumber, [
        // TODO: Pattern
        Validators.maxLength(15)
      ]],
      streetAddress1: [this.formOrgAdministrator.streetAddress1, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      streetAddress2: [this.formOrgAdministrator.streetAddress2, [
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      city: [this.formOrgAdministrator.city, [
        Validators.maxLength(50)
      ]],
      state: [this.formOrgAdministrator.state],
      postalCode: [this.formOrgAdministrator.postalCode, [
        Validators.pattern(AppConstants.OLValidators.PostalCode)
      ]],
      orgRole: [this.formOrgAdministrator.orgRole, [
        Validators.required
      ]]
    });
    for (let key in childForm.controls) {
      this.orgAdministratorForm.addControl(key, childForm.get(key));
    }

    this.orgAdministratorForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.orgAdministratorForm) {
      const form = this.orgAdministratorForm;
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
    if (this.orgAdministratorForm) {
      if (editing) {
        this.orgAdministratorForm.enable();
        this.editing = true;
        this.onEdit.emit(true);
      } else {
        this.orgAdministratorForm.disable();
        this.editing = false;
        this.onEdit.emit(false);
      }
    }
  }

  private getRoles(): void {
    this.roles = Object.keys(AppConstants.Role).map(key => AppConstants.Role[key]);
  }

  private getStates(): void {
    this.states = AppConstants.States;
    this.filteredStates = this.orgAdministratorForm.get('state')
      .valueChanges
      .startWith(null)
      .map(val => val ? this.filterStates(val) : this.states.slice());
  }

  private filterStates(val: string): any[] {
    return this.states.filter(state => new RegExp(`${val}`, 'gi').test(state.name));
  }

  save(): void {
    if (this.orgAdministratorForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.orgAdministratorForm.markAsTouched();
      for (let key in this.orgAdministratorForm.controls) {
        this.orgAdministratorForm.controls[key].markAsTouched();
      }
    }
  }

  private add(): void {
    this.adminService.create(AdminTabs.OrgAdministrator.route, this.orgAdministratorForm.value).subscribe(resp => {
      this.onAdd.emit(resp);
      this.setEditing(false);
      this.notify.success('Successfully added org administrator');
    }, error => {
      this.notify.error('Failed to add org administrator');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminTabs.OrgAdministrator.route, toUpdate).subscribe(resp => {
      this.onUpdate.emit(resp);
      this.setEditing(false);
      this.notify.success('Successfully updated org administrator');
    }, error => {
      this.notify.error('Failed to update org administrator');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formOrgAdministrator.id,
      firstName: this.orgAdministratorForm.get('firstName').value,
      lastName: this.orgAdministratorForm.get('lastName').value,
      login: this.orgAdministratorForm.get('login').value,
      password: this.orgAdministratorForm.get('password').value,
      notes: this.orgAdministratorForm.get('notes').value,
      authority: AppConstants.Role.OrgAdmin.name,
      email: this.orgAdministratorForm.get('email').value,
      phoneNumber: this.orgAdministratorForm.get('phoneNumber').value,
      streetAddress1: this.orgAdministratorForm.get('streetAddress1').value,
      streetAddress2: this.orgAdministratorForm.get('streetAddress2').value,
      city: this.orgAdministratorForm.get('city').value,
      state: this.orgAdministratorForm.get('state').value,
      postalCode: this.orgAdministratorForm.get('postalCode').value,
      organizationId: this.orgAdministratorForm.get('organizationId').value,
      orgRole: this.orgAdministratorForm.get('orgRole').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminTabs.OrgAdministrator.route, this.formOrgAdministrator.id).subscribe(resp => {
      this.onDelete.emit();
      this.notify.success('Successfully deleted org administrator');
    }, error => {
      this.notify.error('Failed to delete org administrator');
    });
  }

  edit(): void {
    this.setEditing(true);
  }

  cancel(): void {
    this.ngOnInit();
    this.onEdit.emit(false);
  }

  resetPassword(changingPassword: boolean): void {
    this.changingPassword = changingPassword;
  }

  displayState(stateValue: string): string {
    return stateValue ? _.filter(AppConstants.States, {value: stateValue})[0].name : '';
  }
}
