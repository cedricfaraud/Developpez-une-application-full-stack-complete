import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../interfaces/topic.interface';

@Injectable({
  providedIn: 'root',
})
export class TopicService {
  private pathService = 'api/topics';

  constructor(private httpClient: HttpClient) {}

  public getAll(): Observable<Topic[]> {
    return this.httpClient.get<Topic[]>(this.pathService);
  }

  public getTopicById(id: string): Observable<Topic> {
    return this.httpClient.get<Topic>(`${this.pathService}/${id}`);
  }
}
