import { PortfolioItem } from './../../../models/portfolio-item.model';
import {Component, Input, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import {saveAs as importedSaveAs} from "file-saver";
import * as _ from "lodash";

import {Assignment} from '../../../models/assignment.model';
import {CourseDialogComponent} from "../course-dialog.component";
import {GradeDialogComponent} from "../grade-dialog.component";
import {FileUploadComponent} from "../../fileupload/fileupload.component";
import {CourseStudentDialogComponent} from "../course-student-dialog.component";
import {AssignmentService} from "../../../services/assignment.service";
import {PortfolioService} from "../../../services/portfolio.service";
import {FileGuardian} from '../../../shared/file-guardian.service';

@Component({
  selector: 'app-files-grid',
  templateUrl: './files-grid.component.html',
  styleUrls: ['./files-grid.component.css']
})
export class FilesGridComponent implements OnInit {

  @Input() assignment: Assignment;
  @Input() portfolio: PortfolioItem;
  @Input() studentView: boolean = false;
  files: any[];
  columns: any[];

  sortColumn: any;
  reverse: boolean;

  public fileUploadSuccessCallback: Function;

  constructor(private dialog: MdDialog,
              private assignmentService: AssignmentService,
              private portfolioService: PortfolioService,
              private fileGuardian: FileGuardian) {}

  ngOnInit(): void {

    this.columns = [
      {
        id: "key",
        name: "Name"
      },
      {
        id: "size",
        name: "Size"
      },
      {
        id: "lastModified",
        name: "Date"
      }
    ];

    this.getFiles();

    this.fileUploadSuccessCallback = this.addFile.bind(this);

  }

  addFile(item): void {
    console.log("File upload success callback");

    if (!this.files) {
      this.files = [];
    }

    this.files.push(
      {
        key: item.file.name,
        size: item.file.size,
        lastModified: new Date().toISOString()
      }
    );
  }

  removeFile(key: String): void {
    if (this.fileGuardian.canHaveFiles(this.assignment)) {
      this.assignmentService.deleteAssignmentFile(this.assignment.id, key).subscribe(resp => {
        this.files = _.filter(this.files, file => file.key !== key);
      });
    } else if (this.fileGuardian.canHaveFiles(this.portfolio)) {
      this.portfolioService.deletePortfolioFile(this.portfolio.id, key).subscribe(resp => {
        this.files = _.filter(this.files, file => file.key !== key);
      });
    }
  }

  getFiles(): void {
    if (this.fileGuardian.canHaveFiles(this.assignment)) {
      this.assignmentService.getAssignmentFiles(this.assignment.id).subscribe(files => {
        this.files = files;
        for (let fileIdx = 0; fileIdx < this.files.length; fileIdx++) {
          files[fileIdx].key = files[fileIdx].key.substring(files[fileIdx].key.indexOf("_", 2)+1);
        }
      });
    } else if (this.fileGuardian.canHaveFiles(this.portfolio)) {
      this.portfolioService.getPortfolioFiles(this.portfolio.id).subscribe(files => {
        this.files = files;
        for (let fileIdx = 0; fileIdx < this.files.length; fileIdx++) {
          files[fileIdx].key = files[fileIdx].key.substring(files[fileIdx].key.indexOf("_", 2)+1);
        }
      });
    }
  }

  getFile(file): void {
    if (this.fileGuardian.canHaveFiles(this.assignment)) {
      let fileName = file.key;
      this.assignmentService.getAssignmentFile(this.assignment.id, fileName).subscribe(blob => {
        importedSaveAs(blob, fileName);
      });;
    } else if (this.fileGuardian.canHaveFiles(this.portfolio)) {
      let fileName = file.key;
      this.portfolioService.getPortfolioFile(this.portfolio.id, fileName).subscribe(blob => {
        importedSaveAs(blob, fileName);
      });;
    }
  }

  stopPropagation(e): void {
    e.stopPropagation();
  }
}
