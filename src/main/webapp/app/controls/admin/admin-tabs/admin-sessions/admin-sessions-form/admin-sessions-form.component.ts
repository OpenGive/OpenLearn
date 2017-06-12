import {Component, Input, OnChanges, OnInit, SimpleChanges} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'admin-sessions-form',
  templateUrl: './admin-sessions-form.component.html',
  styleUrls: ['./admin-sessions-form.component.css', '../../admin-forms.css']
})
export class AdminSessionsFormComponent implements OnInit, OnChanges {

  @Input('item') formSession: any;
  @Input() editing: boolean;

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
      minlength: 'Name must be at least 3 characters long',
      maxlength: 'Name cannot be more than 100 characters long'
    },
    description: {
      minlength: 'Description must be at least 10 characters long',
      maxlength: 'Description cannot be more than 800 characters long'
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

  constructor(public dialogRef: MdDialogRef<AdminSessionsFormComponent>,
              private fb: FormBuilder) { }

  ngOnInit(): void {
    this.buildForm();
    this.changeFormState();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.editing) {
      this.changeFormState();
    }
  }

  changeFormState(): void {
    if (this.sessionForm) {
      if (this.editing) {
        this.sessionForm.enable();
      } else {
        this.sessionForm.disable();
      }
    }
  }

  buildForm(): void {
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
      active: [this.formSession.active]
    });
    this.sessionForm.valueChanges.subscribe(data => this.onValueChanged(data));
    this.onValueChanged();
  }

  onValueChanged(data?: any): void {
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

  displayOrganization(organization): string {
    return organization ? organization.name : '';
  }
}
