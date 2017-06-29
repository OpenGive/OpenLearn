// import {Component, Input, OnInit} from "@angular/core";
// import {FormBuilder, FormGroup, Validators} from "@angular/forms";
// import {MdDialogRef} from "@angular/material";
//
// import {AppConstants} from "../../../../app.constants";
// import {OLDialogComponent} from "../../../../shared/ol-dialog.component";
// import {AdminModel} from "../../admin.constants";
// import {AdminService} from "../../../../services/admin.service";
// import {NotifyService} from "../../../../services/notify.service";
//
// @Component({
//   selector: 'admin-milestones-form',
//   templateUrl: './admin-milestones-form.component.html',
//   styleUrls: ['../../../../shared/ol-forms.css']
// })
// export class AdminMilestonesFormComponent implements OnInit {
//
//   @Input('item') formMilestone: any;
//   @Input() adding: boolean;
//   editing: boolean;
//
//   milestoneForm: FormGroup;
//   formErrors = {
//     name: '',
//     description: '',
//     course: '',
//     points: ''
//   };
//   validationMessages = {
//     name: {
//       required: 'Name is required',
//       minlength: 'Name must be at least 3 characters long',
//       maxlength: 'Name cannot be more than 100 characters long'
//     },
//     description: {
//       minlength: 'Description must be at least 5 characters long',
//       maxlength: 'Description cannot be more than 200 characters long'
//     },
//     course: {
//       required: 'Course is required'
//     },
//     points: {
//       pattern: 'Points must be a non-negative integer'
//     }
//   };
//
//   constructor(public dialogRef: MdDialogRef<OLDialogComponent>,
//               private fb: FormBuilder,
//               private adminService: AdminService,
//               private notify: NotifyService) {}
//
//   ngOnInit(): void {
//     this.buildForm();
//     this.setEditing(this.adding);
//   }
//
//   private buildForm(): void {
//     this.milestoneForm = this.fb.group({
//       name: [this.formMilestone.name, [
//         Validators.required,
//         Validators.minLength(3),
//         Validators.maxLength(100)
//       ]],
//       description: [this.formMilestone.description, [
//         Validators.minLength(5),
//         Validators.maxLength(200)
//       ]],
//       course: [this.formMilestone.course, [
//         Validators.required
//       ]],
//       points: [this.formMilestone.points, [
//         Validators.pattern(AppConstants.OLValidators.Points)
//       ]]
//     });
//     this.milestoneForm.valueChanges.subscribe(data => this.onValueChanged());
//     this.onValueChanged();
//   }
//
//   private onValueChanged(): void {
//     if (this.milestoneForm) {
//       const form = this.milestoneForm;
//       for (const field in this.formErrors) {
//         this.formErrors[field] = '';
//         const control = form.get(field);
//         if (control && control.dirty && !control.valid) {
//           const messages = this.validationMessages[field];
//           for (const key in control.errors) {
//             this.formErrors[field] += messages[key] + ' ';
//           }
//         }
//       }
//     }
//   }
//
//   private setEditing(editing: boolean): void {
//     if (this.milestoneForm) {
//       if (editing) {
//         this.milestoneForm.enable();
//         this.editing = true;
//       } else {
//         this.milestoneForm.disable();
//         this.editing = false;
//       }
//     }
//   }
//
//   save(): void {
//     if (this.milestoneForm.valid) {
//       if (this.adding) {
//         this.add();
//       } else {
//         this.update();
//       }
//     } else {
//       this.milestoneForm.markAsTouched();
//     }
//   }
//
//   private add(): void {
//     this.adminService.create(AdminModel.Milestone.route, this.milestoneForm.value).subscribe(resp => {
//       this.dialogRef.close({
//         type: 'ADD',
//         data: resp
//       });
//       this.notify.success('Successfully added milestone');
//     }, error => {
//       this.notify.error('Failed to add milestone');
//     });
//   }
//
//   private update(): void {
//     const toUpdate = this.prepareToUpdate();
//     this.adminService.update(AdminModel.Milestone.route, toUpdate).subscribe(resp => {
//       this.dialogRef.close({
//         type: 'UPDATE',
//         data: resp
//       });
//       this.notify.success('Successfully updated milestone');
//     }, error => {
//       this.notify.error('Failed to update milestone');
//     });
//   }
//
//   private prepareToUpdate(): any {
//     return {
//       id: this.formMilestone.id,
//       name: this.milestoneForm.get('name').value,
//       description: this.milestoneForm.get('description').value,
//       course: this.milestoneForm.get('course').value,
//       points: this.milestoneForm.get('points').value
//     };
//   }
//
//   delete(): void {
//     this.adminService.delete(AdminModel.Milestone.route, this.formMilestone.id).subscribe(resp => {
//       this.dialogRef.close({
//         type: 'DELETE',
//         data: {
//           id: this.formMilestone.id
//         }
//       });
//       this.notify.success('Successfully deleted milestone');
//     }, error => {
//       this.notify.error('Failed to delete milestone');
//     });
//   }
//
//   edit(): void {
//     this.setEditing(true);
//   }
//
//   cancel(): void {
//     this.ngOnInit();
//   }
//
//   close(): void {
//     this.dialogRef.close();
//   }
//
//   displayCourse(course: any): string {
//     return course ? course.name : '';
//   }
// }
