import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostComment } from '../interfaces/comment.interface';
import { CommentRequest } from '../interfaces/commentRequest.interface';
import { Post } from '../interfaces/post.interface';
import { PostRequest } from '../interfaces/postRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private pathService = 'api/posts';

  constructor(private httpClient: HttpClient) {}

  public createPost(postRequest: PostRequest): Observable<void> {
    const body = {
      topicId: postRequest.topicId,
      title: postRequest.title,
      content: postRequest.content,
    };
    return this.httpClient.post<void>(`${this.pathService}`, body);
  }

  public getAllPosts(): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.pathService);
  }

  public getPostById(id: string): Observable<Post> {
    return this.httpClient.get<Post>(`${this.pathService}/${id}`);
  }

  public addPostComment(
    id: string,
    commentRequest: CommentRequest
  ): Observable<PostComment> {
    const body = { content: commentRequest.content };
    return this.httpClient.post<PostComment>(
      `${this.pathService}/${id}/comment`,
      body
    );
  }
  public getCommentsByPostId(id: string): Observable<PostComment[]> {
    return this.httpClient.get<PostComment[]>(
      `${this.pathService}/${id}/comments`
    );
  }
}
