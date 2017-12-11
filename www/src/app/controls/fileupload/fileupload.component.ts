
import { Component, Input, Inject} from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { CookieService } from 'ngx-cookie';
import {Course} from '../../models/course.model';
import {MdDialog, MdDialogRef, MD_DIALOG_DATA} from "@angular/material";

import {FileGuardian} from '../../shared/file-guardian.service';

@Component({
  selector: 'file-upload',
  templateUrl: './fileupload.component.html',
  styleUrls: ['./fileupload.component.css', '../dialog-forms.css']
})
export class FileUploadComponent {

    public uploader:FileUploader = new FileUploader({});
    public hasBaseDropZoneOver:boolean = false;
    public hasAnotherDropZoneOver:boolean = false;

    @Input()
    public studentView: boolean = false;

    @Input()
    public onSuccessCallback: Function;

    @Input() public assignment: any;
    @Input() public portfolio: any;

    columns: any[];

    constructor(
            private dialog: MdDialogRef<FileUploadComponent>,
            private _cookieService: CookieService,
            private fileGuardian: FileGuardian) {
        let tokenObject = this._cookieService.getObject('token') as any;
        this.uploader.authToken = 'Bearer ' + tokenObject.access_token;
        this.uploader.onSuccessItem = (item: any, response: any, status: any, headers: any) => {
          this.onSuccessCallback(item);
        }

        this.columns = [
          {
            id: "file.name",
            name: "Name"
          },
          {
            id: "file.date",
            name: "Date"
          },
          {
            id: "file.size",
            name: "Size"
          }
        ];
    }

    ngOnInit(): void {
      let uploadUrl = '';
      if (this.fileGuardian.canHaveFiles(this.assignment)) {
        uploadUrl = '/api/assignments/' + this.assignment.id.toString() + '/upload';
      } else if (this.fileGuardian.canHaveFiles(this.portfolio)) {
        uploadUrl = '/api/portfolio-items/' + this.portfolio.id.toString() + '/upload';
      }
      this.uploader.options.url = uploadUrl;
    }

    public fileOverBase(e:any):void {
        this.hasBaseDropZoneOver = e;
    }

    public fileOverAnother(e:any):void {
        this.hasAnotherDropZoneOver = e;
    }

    close(): void {
      this.dialog.close();
    }
}
