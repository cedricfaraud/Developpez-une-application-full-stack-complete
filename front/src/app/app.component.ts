import { Component, OnInit } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { User } from './interfaces/user.interface';
import { AuthService } from './services/auth.service';
import { SessionService } from './services/session.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'front';
  public showHeader: boolean = false;
  public activePage!: string;

  constructor(
    private authService: AuthService,
    private router: Router,
    private sessionService: SessionService
  ) {
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
  ngOnInit(): void {
    this.autoLog();
  }
  /**
   * If token is into the localStorage autologin, else page home
   */
  public autoLog(): void {
    this.authService.me().subscribe(
      (user: User) => {
        this.sessionService.logIn(user);
        this.router.navigate(['posts']);
      },
      (_) => {
        this.sessionService.logOut();
        this.router.navigate(['']);
      }
    );
  }
}
