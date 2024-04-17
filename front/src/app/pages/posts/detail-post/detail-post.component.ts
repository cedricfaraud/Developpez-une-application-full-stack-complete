import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { take } from 'rxjs';
import { PostComment } from 'src/app/interfaces/comment.interface';
import { CommentRequest } from 'src/app/interfaces/commentRequest.interface';
import { Post } from 'src/app/interfaces/post.interface';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-detail-post',
  templateUrl: './detail-post.component.html',
  styleUrl: './detail-post.component.scss',
})
export class DetailPostComponent {
  public postId!: string;
  public post!: Post;
  public addCommentForm = this.fb.group({
    content: ['', [Validators.required, Validators.minLength(10)]],
  });

  constructor(
    private postService: PostService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.postId = this.route.snapshot.paramMap.get('id')!;
    this.getPost();
    this.getComments();
  }

  public getPost(): void {
    this.postService
      .getPostById(this.postId)
      .pipe(take(1))
      .subscribe((post: Post) => {
        this.post = post;
      });
  }

  public getComments(): void {
    this.postService
      .getCommentsByPostId(this.postId)
      .subscribe((comments: PostComment[]) => {
        this.post.comments = comments;
      });
  }

  public onSubmitComment(): void {
    const commentRequest = this.addCommentForm
      .value as unknown as CommentRequest;
    this.postService.addPostComment(this.postId, commentRequest).subscribe({
      next: (_) => {
        this.getComments();
      },
    });
  }
}
