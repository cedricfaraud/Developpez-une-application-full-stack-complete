import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-header',

  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  public isLogged$: Observable<boolean> = this.sessionService.$isLogged();
  // get page from app-component in order to modify link active
  @Input() page!: string;

  constructor(private router: Router, private sessionService: SessionService) {}

  public navToProfile(): void {
    this.router.navigateByUrl('profile');
  }
  public navToPost(): void {
    this.router.navigateByUrl('posts');
  }

  public navToTopic(): void {
    this.router.navigateByUrl('topics');
  }
}
