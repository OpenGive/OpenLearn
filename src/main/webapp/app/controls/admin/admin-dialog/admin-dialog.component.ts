import {Component, Inject, OnInit} from "@angular/core";
import {MdDialogRef, MD_DIALOG_DATA} from "@angular/material";
import {AdminService} from "../../../services/admin.service";
import {cloneDeep} from "lodash";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'admin-dialog',
  templateUrl: './admin-dialog.component.html',
  styleUrls: ['./admin-dialog.component.css']
})
export class AdminDialogComponent implements OnInit {

  // Data from caller
  adding: boolean;
  tab: string;
  title: string;
  copy: any;

  editing: boolean;

  private userTabs = ['administrators', 'instructors', 'students'];

  constructor(@Inject(MD_DIALOG_DATA) public data: any,
              public dialogRef: MdDialogRef<AdminDialogComponent>,
              private adminService: AdminService,
              private userService: UserService) {}

  ngOnInit() {
    this.transferData();
  }

  transferData(): void {
    this.adding = this.data.adding;
    this.editing = this.data.adding;
    this.tab = this.data.tab;
    this.title = this.data.title;
    this.cloneItem();
  }

  cloneItem(): void {
    this.copy = cloneDeep(this.data.item);
  }

  edit(): void {
    this.editing = true;
  }

  cancel(exit: boolean): void {
    if (this.adding || exit) {
      this.dialogRef.close();
    } else {
      this.editing = false;
      this.cloneItem();
    }
  }

  save(): void {
    if (this.adding) {
      this.create();
    } else {
      this.update();
    }
  }

  create(): void {
    if (this.userTabs.includes(this.tab)) {
      this.userService.create(this.copy).subscribe(resp => this.handleCreateResponse(resp));
    } else {
      this.adminService.create(this.tab, this.copy).subscribe(resp => this.handleCreateResponse(resp));
    }
  }

  handleCreateResponse(resp): void {
    this.dialogRef.close({
      type: 'add',
      data: resp
    });
  }

  update(): void {
    if (this.userTabs.includes(this.tab)) {
      this.userService.update(this.copy).subscribe(resp => this.handleUpdateResponse(resp));
    } else {
      this.adminService.update(this.tab, this.copy).subscribe(resp => this.handleUpdateResponse(resp));
    }
  }

  handleUpdateResponse(resp): void {
    this.dialogRef.close({
      type: 'update',
      data: resp
    });
  }

  delete() {
    if (this.userTabs.includes(this.tab)) {
      this.userService.delete(this.copy.id).subscribe(resp => this.handleDeleteResponse(resp, this.copy.id));
    } else {
      this.adminService.delete(this.tab, this.copy.id).subscribe(resp => this.handleDeleteResponse(resp, this.copy.id));
    }
  }

  handleDeleteResponse(resp, id): void {
    this.dialogRef.close({
      type: 'delete',
      data: id
    });
  }
}
