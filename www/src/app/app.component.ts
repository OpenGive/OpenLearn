import {Component, OnInit} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {environment} from "../environments/environment";

declare var window;
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  private title = 'OpenLearn';

  constructor(private titleService: Title) {}

  ngOnInit() {
    const prefix = environment.name !== 'PROD' ? (environment.name + ' ') : '';
    this.titleService.setTitle(prefix + this.title);
    window.loading_screen.finish();
  }
}
