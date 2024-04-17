import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Post } from 'src/app/interfaces/post.interface';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
})
export class PostComponent {
  @Input() post!: Post;

  constructor(private router: Router) {}

  public showDetail(id: number): void {
    this.router.navigateByUrl('/detail-post/' + id);
  }
}
