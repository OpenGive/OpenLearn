
import { Component, Input, Inject} from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { CookieService } from 'ngx-cookie';
import {Course} from '../../models/course.model';
import {MdDialog, MdDialogRef, MD_DIALOG_DATA} from "@angular/material";

// const URL = '/api/';
const URL = '/api/courses/{id}/upload';

@Component({
  selector: 'file-upload',
  templateUrl: './fileupload.component.html',
  styleUrls: ['./fileupload.component.css', '../dialog-forms.css']
})
export class FileUploadComponent {

    public uploader:FileUploader = new FileUploader({url: URL});
    public hasBaseDropZoneOver:boolean = false;
    public hasAnotherDropZoneOver:boolean = false;

    columns: any[];

    constructor(
            @Inject(MD_DIALOG_DATA) public data: any,
            private dialog: MdDialogRef<FileUploadComponent>,
            private _cookieService: CookieService) {
        let tokenObject = this._cookieService.getObject('token') as any;
        this.uploader.authToken = 'Bearer ' + tokenObject.access_token;
        this.uploader.options.url = URL.replace('{id}', data.course.id.toString())

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
