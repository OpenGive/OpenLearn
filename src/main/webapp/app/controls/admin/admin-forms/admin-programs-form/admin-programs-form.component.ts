import {Component, Input, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MdDialogRef} from "@angular/material";
import {Observable} from "rxjs/Observable";

import {AdminDialogComponent} from "../../admin-dialog.component";
import {AdminModel} from "../../admin.constants";
import {AdminService} from "../../../../services/admin.service";
import {NotifyService} from "../../../../services/notify.service";

@Component({
  selector: 'admin-programs-form',
  templateUrl: './admin-programs-form.component.html',
  styleUrls: ['../admin-forms.css']
})
export class AdminProgramsFormComponent implements OnInit {

  @Input('item') formProgram: any;
  @Input() adding: boolean;
  editing: boolean;

  schools: any[];
  sessions: any[];

  filteredSchools: Observable<any[]>;
  filteredSessions: Observable<any[]>;

  programForm: FormGroup;
  formErrors = {
    name: '',
    session: ''
  };
  validationMessages = {
    name: {
      required: 'Name is required',
      minlength: 'Name must be at least 5 characters long',
      maxlength: 'Name cannot be more than 50 characters long'
    },
    session: {
      required: 'Session is required'
    }
  };

  constructor(public dialogRef: MdDialogRef<AdminDialogComponent>,
              private fb: FormBuilder,
              private adminService: AdminService,
              private notify: NotifyService) {}

  ngOnInit(): void {
    this.buildForm();
    this.setEditing(this.adding);
    this.getSchools();
    this.getSessions();
  }

  private buildForm(): void {
    this.programForm = this.fb.group({
      name: [this.formProgram.name, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(50)
      ]],
      description: [this.formProgram.description],
      session: [this.formProgram.session, [
        Validators.required
      ]],
      school: [this.formProgram.school],
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

  private getSchools(): void {
    this.adminService.getAll(AdminModel.School.route).subscribe(resp => {
      this.schools = resp;
      this.filteredSchools = this.programForm.get('school')
        .valueChanges
        .startWith(null)
        .map(val => val ? this.filterSchools(val) : this.schools.slice());
    });
  }

  private filterSchools(val: string): any[] {
    return this.schools.filter(school => new RegExp(`${val}`, 'gi').test(school.name));
  }

  private getSessions(): void {
    this.adminService.getAll(AdminModel.Session.route).subscribe(resp => {
      this.sessions = resp;
      this.filteredSessions = this.programForm.get('session')
        .valueChanges
        .startWith(null)
        .map(val => val ? this.filterSessions(val) : this.sessions.slice());
    });
  }

  private filterSessions(val: string): any[] {
    return this.sessions.filter(session => new RegExp(`${val}`, 'gi').test(session.name));
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
    this.adminService.create(AdminModel.Program.route, this.programForm.value).subscribe(resp => {
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
    this.adminService.update(AdminModel.Program.route, toUpdate).subscribe(resp => {
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
      session: this.programForm.get('session').value,
      school: this.programForm.get('school').value,
      active: this.programForm.get('active').value
    };
  }

  delete(): void {
    this.adminService.delete(AdminModel.Program.route, this.formProgram.id).subscribe(resp => {
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

  displaySchool(school: any): string {
    return school ? school.name : '';
  }

  displaySession(session: any): string {
    return session ? session.name : '';
  }
}
