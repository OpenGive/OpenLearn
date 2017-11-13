
import { Component } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { CookieService } from 'ngx-cookie';

// const URL = '/api/';
const URL = '/api/courses/upload';

@Component({
  selector: 'file-upload',
  templateUrl: './fileupload.component.html',
  styleUrls: ['./fileupload.component.css']
})
export class FileUploadComponent {

    public uploader:FileUploader = new FileUploader({url: URL});
    public hasBaseDropZoneOver:boolean = false;
    public hasAnotherDropZoneOver:boolean = false;

    columns: any[];

    constructor(private _cookieService: CookieService) {
        let tokenObject = this._cookieService.getObject('token') as any;
        this.uploader.authToken = 'Bearer ' + tokenObject.access_token;

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

}
