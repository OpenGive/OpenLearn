
import { Component } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { CookieService } from 'ngx-cookie';

// const URL = '/api/';
const URL = '/api/courses/upload';

@Component({
  selector: 'file-upload',
  templateUrl: './fileupload.component.html'
})
export class FileUploadComponent {

    public uploader:FileUploader = new FileUploader({url: URL});
    public hasBaseDropZoneOver:boolean = false;
    public hasAnotherDropZoneOver:boolean = false;

    constructor(private _cookieService: CookieService) {
        let tokenObject = this._cookieService.getObject('token') as any;
        this.uploader.authToken = 'Bearer ' + tokenObject.access_token;
    }

    public fileOverBase(e:any):void {
        this.hasBaseDropZoneOver = e;
    }

    public fileOverAnother(e:any):void {
        this.hasAnotherDropZoneOver = e;
    }

}
