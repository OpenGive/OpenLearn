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
import {NotifyService} from "../../../services/notify.service";

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
  studentFiles: any[];
  columns: any[];

  sortColumn: any;
  reverse: boolean;

  public fileUploadSuccessCallback: Function;
  public studentFileUploadSuccessCallback: Function;

  private getAssignmentFilesFunction: Function;

  constructor(private dialog: MdDialog,
              private assignmentService: AssignmentService,
              private notify: NotifyService,
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


    if (!this.studentView)
      this.getAssignmentFilesFunction =  this.assignmentService.getAssignmentFiles.bind(this.assignmentService);
    else
      this.getAssignmentFilesFunction =  this.assignmentService.getAssignmentInstructorFiles.bind(this.assignmentService);

    this.files = [];
    this.studentFiles = [];
    this.getFiles();
    this.getStudentFiles();
    console.log(this.studentFiles);

    this.fileUploadSuccessCallback = this.addFileCallback.bind(this);
    this.studentFileUploadSuccessCallback = this.addStudentFileCallback.bind(this);

  }

  addFileCallback(item): void {
    this.addFile(item, this.files);
  }

  addStudentFileCallback(item): void {
    this.addFile(item, this.studentFiles);
  }

  addFile(item, files): void {
    console.log(item);
    console.log("File upload success callback");

    item.key = this.parseBaseName(item.fileUrl);
    files.push(item);
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
      this.getAssignmentFilesFunction(this.assignment.id).subscribe(files => {
        this.files = this.addFileKeys(files);
      });
    } else if (this.fileGuardian.canHaveFiles(this.portfolio)) {
      this.portfolioService.getPortfolioFiles(this.portfolio.id).subscribe(files => {
        this.files = this.addFileKeys(files);
      });
    }
  }

  getFile(file): void {
    if (this.fileGuardian.canHaveFiles(this.assignment)) {
      this.assignmentService.getAssignmentFile(this.assignment.id, file.id).subscribe(blob => {
        importedSaveAs(blob, file.key);
      }, this.errorOnDownload.bind(this));
    } else if (this.fileGuardian.canHaveFiles(this.portfolio)) {
      this.portfolioService.getPortfolioFile(this.portfolio.id, file.id).subscribe(blob => {
        importedSaveAs(blob, file.key);
      }, this.errorOnDownload.bind(this));
    }
  }

  getStudentFiles(): void {
    if (!this.studentView || !this.fileGuardian.canHaveFiles(this.assignment))
      return

    this.assignmentService.getAssignmentFiles(this.assignment.id).subscribe(files => {
      this.studentFiles = this.addFileKeys(files);
      console.log(this.studentFiles);
    }, this.errorOnLoadingFiles.bind(this));
  }

  stopPropagation(e): void {
    e.stopPropagation();
  }

  private errorOnLoadingFiles(error: Response): void {
    this.notify.error("An error occurred while attempting to retrieve all files.");
    console.error(error);
  }

  private errorOnDownload(error: Response): void {
    this.notify.error("An error occurred, the file could not be downloaded.");
    console.error(error);
  }

  private addFileKeys(files: any[]): any[] {
    for (let file of files) {
      file.key = this.parseBaseName(file.fileUrl);
    }

    return files;
  }

  private parseBaseName(path: String): String {
    return path.substr(path.lastIndexOf("/")+1)
  }
}
