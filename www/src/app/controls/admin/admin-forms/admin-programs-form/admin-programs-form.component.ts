import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AdminTabs} from "../../admin.constants";
import {AdminService} from "../../../../services/admin.service";
import {NotifyService} from "../../../../services/notify.service";

@Component({
  selector: 'admin-programs-form',
  templateUrl: './admin-programs-form.component.html',
  styleUrls: ['../../../dialog-forms.css']
})
export class AdminProgramsFormComponent implements OnInit {

  @Input('item') formProgram: any;
  @Input() adding: boolean;
  editing: boolean;

  organizations: any[];

  filteredOrganizations: Observable<any[]>;

  programForm: FormGroup;
  formErrors = {
    name: '',
    description: '',
    organization: '',
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
    this.programForm = this.fb.group({
      name: [this.formProgram.name, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      description: [this.formProgram.description, [
        Validators.minLength(5),
        Validators.maxLength(200)
      ]],
      organization: [this.formProgram.organization, [
        Validators.required
      ]],
      active: [this.formProgram.active || false]
    });
    this.programForm.valueChanges.subscribe(data => this.onValueChanged());
    this.onValueChanged();
  }

  private onValueChanged(): void {
    if (this.programForm) {
      const form = this.programForm;
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
    if (this.programForm) {
      if (editing) {
        this.programForm.enable();
        this.editing = true;
      } else {
        this.programForm.disable();
        this.editing = false;
      }
    }
  }

  private getOrganizations(): void {
    this.adminService.getAll(AdminTabs.Organization.route).subscribe(resp => {
      this.organizations = resp;
      this.filteredOrganizations = this.programForm.get('organization')
        .valueChanges
        .startWith(null)
        .map(val => val ? this.filterOrganizations(val) : this.organizations.slice());
    });
  }

  private filterOrganizations(val: string): any[] {
    return this.organizations.filter(organization => new RegExp(`${val}`, 'gi').test(organization.name));
  }

  save(): void {
    if (this.programForm.valid) {
      if (this.adding) {
        this.add();
      } else {
        this.update();
      }
    } else {
      this.programForm.markAsTouched();
    }
  }

  private add(): void {
    this.adminService.create(AdminTabs.Program.route, this.programForm.value).subscribe(resp => {
      this.dialogRef.close({
        type: 'ADD',
        data: resp
      });
      this.notify.success('Successfully added program');
    }, error => {
      this.notify.error('Failed to add program');
    });
  }

  private update(): void {
    const toUpdate = this.prepareToUpdate();
    this.adminService.update(AdminTabs.Program.route, toUpdate).subscribe(resp => {
      this.dialogRef.close({
        type: 'UPDATE',
        data: resp
      });
      this.notify.success('Successfully updated program');
    }, error => {
      this.notify.error('Failed to update program');
    });
  }

  private prepareToUpdate(): any {
    return {
      id: this.formProgram.id,
      name: this.programForm.get('name').value,
      description: this.programForm.get('description').value,
      organization: this.programForm.get('organization').value,
      active: this.programForm.get('active').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminTabs.Program.route, this.formProgram.id).subscribe(resp => {
      this.dialogRef.close({
        type: 'DELETE',
        data: {
          id: this.formProgram.id
        }
      });
      this.notify.success('Successfully deleted program');
    }, error => {
      this.notify.error('Failed to delete program');
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
