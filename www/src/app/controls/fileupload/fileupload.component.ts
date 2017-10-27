import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { Http } from '@angular/http';
import {AdminService} from "../../services/admin.service";

@Component({
    selector: 'file-upload',
    template: '<input type="file" [multiple]="multiple" #fileInput><br/>><button (click)="upload()">Upload</button>'
})
export class FileUploadComponent {
    @Input() multiple: boolean = false;
    @ViewChild('fileInput') inputEl: ElementRef;

    constructor(private http: Http,
        private adminService: AdminService) {}

    upload() {
        let inputEl: HTMLInputElement = this.inputEl.nativeElement;
        let fileCount: number = inputEl.files.length;
        let formData = new FormData();
        if (fileCount > 0) { // a file was selected
            for (let i = 0; i < fileCount; i++) {
                formData.append('file[]', inputEl.files.item(i));
            }
            this.adminService.upload('courses/upload', formData).subscribe(resp => {console.log(resp)});
                // do whatever you do...
                // subscribe to observable to listen for response
        }
    }
}