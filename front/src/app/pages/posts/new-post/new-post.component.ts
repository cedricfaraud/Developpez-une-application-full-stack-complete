import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { PostRequest } from 'src/app/interfaces/postRequest.interface';
import { Topic } from 'src/app/interfaces/topic.interface';
import { PostService } from 'src/app/services/post.service';
import { TopicService } from 'src/app/services/topic.service';

@Component({
  selector: 'app-new-post',
  templateUrl: './new-post.component.html',
  styleUrl: './new-post.component.scss',
})
export class NewPostComponent {
  public onError = false;
  public topics$!: Observable<Topic[]>;
  private createPostSub!: Subscription;

  public form = this.fb.group({
    topicId: ['', [Validators.required]],
    title: [
      '',
      [Validators.required, Validators.minLength(5), Validators.maxLength(255)],
    ],
    content: ['', [Validators.required, Validators.minLength(10)]],
  });
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private topicService: TopicService,
    private postService: PostService,
    private matSnackBar: MatSnackBar
  ) {
    this.topics$ = this.topicService.getAll();
  }

  public submit(): void {
    const postRequest = this.form.value as unknown as PostRequest;
    this.createPostSub = this.postService.createPost(postRequest).subscribe({
      next: (_) => {
        this.router.navigate(['/posts']);
        this.matSnackBar.open('Article créé avec succès !', 'Close', {
          duration: 3000,
        });
      },
      error: (_error: any) => {
        this.onError = true;
        this.matSnackBar.open(
          "Erreur lors la création de l'article!",
          'Close',
          { duration: 3000 }
        );
      },
    });
  }
}
