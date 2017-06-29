import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AdminModel} from "../../admin.constants";
import {AdminService} from "../../../../services/admin.service";
import {NotifyService} from "../../../../services/notify.service";

@Component({
  selector: 'admin-sessions-form',
  templateUrl: './admin-sessions-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminSessionsFormComponent implements OnInit {

  @Input('item') formSession: any;
  @Input() adding: boolean;
  editing: boolean;

  organizations: any[];

  filteredOrganizations: Observable<any[]>;

  sessionForm: FormGroup;
  formErrors = {
    name: '',
    description: '',
    organization: '',
    startDate: '',
    endDate: ''
  };
  validationMessages = {
    name: {
      required: 'Name is required',
      minlength: 'Name must be at least 5 characters long',
      maxlength: 'Name cannot be more than 100 characters long'
    },
    description: {
      minlength: 'Description must be at least 5 characters long',
      maxlength: 'Description cannot be more than 200 characters long'
    },
    organization: {
      required: 'Organization is required'
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
              private notify: NotifyService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.getOrganizations();
  }

  private buildForm(): void {
    this.sessionForm = this.fb.group({
      name: [this.formSession.name, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      description: [this.formSession.description, [
        Validators.minLength(5),
        Validators.maxLength(200)
      ]],
      organization: [this.formSession.organization, [
        Validators.required
      ]],
      startDate: [this.formSession.startDate],
      endDate: [this.formSession.endDate],
      active: [this.formSession.active || false]
    });
    this.sessionForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.sessionForm) {
      const form = this.sessionForm;
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
    if (this.sessionForm) {
      if (editing) {
        this.sessionForm.enable();
        this.editing = true;
      } else {
        this.sessionForm.disable();
        this.editing = false;
      }
    }
  }

  private getOrganizations(): void {
    this.adminService.getAll(AdminModel.Organization.route).subscribe(resp => {
      this.organizations = resp;
      this.filteredOrganizations = this.sessionForm.get('organization')
        .valueChanges
        .startWith(null)
        .map(val => val ? this.filterOrganizations(val) : this.organizations.slice());
    });
  }

  private filterOrganizations(val: string): any[] {
    return this.organizations.filter(organization => new RegExp(`${val}`, 'gi').test(organization.name));
  }

  save(): void {
    if (this.sessionForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.sessionForm.markAsTouched();
    }
  }

  private add(): void {
    this.adminService.create(AdminModel.Session.route, this.sessionForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added session');
    }, error => {
      this.notify.error('Failed to add session');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminModel.Session.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated session');
    }, error => {
      this.notify.error('Failed to update session');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formSession.id,
      name: this.sessionForm.get('name').value,
      description: this.sessionForm.get('description').value,
      organization: this.sessionForm.get('organization').value,
      startDate: this.sessionForm.get('startDate').value,
      endDate: this.sessionForm.get('endDate').value,
      active: this.sessionForm.get('active').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminModel.Session.route, this.formSession.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formSession.id
        }
      });
      this.notify.success('Successfully deleted session');
    }, error => {
      this.notify.error('Failed to delete session');
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

  displayOrganization(organization: any): string {
    return organization ? organization.name : '';
  }
}
