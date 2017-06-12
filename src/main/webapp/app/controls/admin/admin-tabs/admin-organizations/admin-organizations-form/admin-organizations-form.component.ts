import {Component, Input, OnChanges, OnInit, SimpleChanges} from "@angular/core";
import {MdDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import * as _ from "lodash";

@Component({
  selector: 'admin-organizations-form',
  templateUrl: './admin-organizations-form.component.html',
  styleUrls: ['./admin-organizations-form.component.css', '../../admin-forms.css']
})
export class AdminOrganizationsFormComponent implements OnInit, OnChanges {

  organizationForm: FormGroup;

  @Input('item') formOrganization: any;
  @Input() editing: boolean;

  constructor(public dialogRef: MdDialogRef<AdminOrganizationsFormComponent>,
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
    if (this.organizationForm) {
      if (this.editing) {
        this.organizationForm.enable();
      } else {
        this.organizationForm.disable();
      }
    }
  }

  buildForm(): void {
    this.organizationForm = this.fb.group({
      name: [this.formOrganization.name, [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(100)
        ]
      ],
      description: [this.formOrganization.description, [
          Validators.minLength(10),
          Validators.maxLength(800)
        ]
      ]
    });
    this.organizationForm.valueChanges.subscribe(data => this.onValueChanged(data));
    this.onValueChanged();
  }

  onValueChanged(data?: any): void {
    if (this.organizationForm) {
      const form = this.organizationForm;
      for (const field in this.formErrors) {
        this.formErrors[field] = '';
        const control = form.get(field);
        if (control && control.dirty && !control.valid) {
          const messages = this.validationMessages[field];
          this.formErrors[field] = _.map(control.errors, key => messages[key]).join(' ');
        }
      }
    }
  }

  formErrors = {
    name: '',
    description: ''
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
    }
  };
}
