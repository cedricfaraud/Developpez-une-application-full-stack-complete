import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from 'src/app/interfaces/post.interface';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.scss',
})
export class PostListComponent implements OnInit {
  public posts$!: Observable<Post[]>;
  constructor(private postService: PostService) {
    this.getPosts();
  }
  ngOnInit(): void {
    this.getPosts();
  }
  public getPosts(): void {
    this.posts$ = this.postService.getAllPosts();
  }
}
