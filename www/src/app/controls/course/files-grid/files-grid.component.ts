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
        id: "uploadedByUser",
        name: "Uploaded By"
      },
      {
        id: "created_date",
        name: "Uploaded At"
      }
    ];

    this.getFiles();

    this.fileUploadSuccessCallback = this.addFile.bind(this);

  }

  addFile(item): void {
    console.log(item);
    console.log("File upload success callback");

    if (!this.files) {
      this.files = [];
    }

    this.files.push(
      {
        key: this.parseBaseName(item.fileUrl),
        uploadedByUser: { firstName: item.uploadedByUser.firstName },
        created_date: new Date().toISOString()
      }
    );
  }

  removeFile(file): void {
    if (this.fileGuardian.canHaveFiles(this.assignment)) {
      this.assignmentService.deleteAssignmentFile(this.assignment.id, file.id).subscribe(resp => {
        this.files = _.filter(this.files, f => f.id !== file.id);
      });
    } else if (this.fileGuardian.canHaveFiles(this.portfolio)) {
      this.portfolioService.deletePortfolioFile(this.portfolio.id, file.id).subscribe(resp => {
        this.files = _.filter(this.files, f => f.id !== file.id);
      });
    }
  }

  getFiles(): void {
    if (this.fileGuardian.canHaveFiles(this.assignment)) {
      this.assignmentService.getAssignmentFiles(this.assignment.id).subscribe(files => {
        this.files = files;
        for (let fileIdx = 0; fileIdx < this.files.length; fileIdx++) {
          const fileUrl = this.files[fileIdx].fileUrl
          this.files[fileIdx].key = this.parseBaseName(fileUrl);
        }
      });
    } else if (this.fileGuardian.canHaveFiles(this.portfolio)) {
      this.portfolioService.getPortfolioFiles(this.portfolio.id).subscribe(files => {
        this.files = files;
        console.log(files);
        for (let fileIdx = 0; fileIdx < this.files.length; fileIdx++) {
          const fileUrl = this.files[fileIdx].fileUrl
          this.files[fileIdx].key = this.parseBaseName(fileUrl);
        }
      });
    }
  }

  getFile(file): void {
    if (this.fileGuardian.canHaveFiles(this.assignment)) {
      this.assignmentService.getAssignmentFile(this.assignment.id, file.id).subscribe(blob => {
        importedSaveAs(blob, file.key);
      });;
    } else if (this.fileGuardian.canHaveFiles(this.portfolio)) {
      this.portfolioService.getPortfolioFile(this.portfolio.id, file.id).subscribe(blob => {
        importedSaveAs(blob, file.key);
      });;
    }
  }

  stopPropagation(e): void {
    e.stopPropagation();
  }

  private parseBaseName(path: String): String {
    return path.substr(path.lastIndexOf("/")+1)
  }
}
