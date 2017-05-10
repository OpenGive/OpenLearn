import { Component, OnInit } from '@angular/core';

declare var window;
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  ngOnInit() {
    window.loading_screen.finish();
  }
}
