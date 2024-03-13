import { Component } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'front';
  public showHeader: boolean = false;
  public activePage!: string;

  constructor(private router: Router) {
    // on route change to '', set the variable showHead to false
    router.events.forEach((event) => {
      if (event instanceof NavigationStart) {
        // retrieve url to pass to header for some style
        this.activePage = event['url'];
        if (event['url'] == '/') {
          this.showHeader = false;
        } else {
          this.showHeader = true;
        }
      }
    });
  }
}
