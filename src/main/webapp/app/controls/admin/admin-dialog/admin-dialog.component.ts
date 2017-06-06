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

  transferData() {
    this.adding = this.data.adding;
    this.editing = this.data.adding;
    this.tab = this.data.tab;
    this.title = this.data.title;
    this.cloneItem();
  }

  cloneItem() {
    this.copy = cloneDeep(this.data.item);
  }

  edit() {
    this.editing = true;
  }

  cancel(exit: boolean) {
    if (this.adding || exit) {
      this.dialogRef.close();
    } else {
      this.editing = false;
      this.cloneItem();
    }
  }

  save() {
    if (this.adding) {
      this.create();
    } else {
      this.update();
    }
  }

  create() {
    if (this.userTabs.includes(this.tab)) {
      this.userService.create(this.copy).subscribe(resp => console.log(resp));
    } else {
      this.adminService.create(this.tab, this.copy).subscribe(resp => console.log(resp));
    }
  }

  update() {
    if (this.userTabs.includes(this.tab)) {
      this.userService.update(this.copy).subscribe(resp => this.dialogRef.close(resp));
    } else {
      this.adminService.update(this.tab, this.copy).subscribe(resp => this.dialogRef.close(resp));
    }
  }

  delete() {
    if (this.userTabs.includes(this.tab)) {
      this.userService.delete(this.copy.id).subscribe(resp => this.dialogRef.close(resp));
    } else {
      this.adminService.delete(this.tab, this.copy.id).subscribe(resp => this.dialogRef.close(resp));
    }
  }
}
