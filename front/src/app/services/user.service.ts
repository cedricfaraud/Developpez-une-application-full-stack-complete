import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../interfaces/topic.interface';
import { User } from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private pathService = 'api/user';

  constructor(private httpClient: HttpClient) {}

  public getUserById(id: string): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/${id}`);
  }

  public unsubscribeTopic(id: string): Observable<any> {
    return this.httpClient.delete(`${this.pathService}/${id}/unsubscribe`);
  }
  public subscribeTopic(id: string): Observable<any> {
    return this.httpClient.post(`${this.pathService}/${id}/subscribe`, '');
  }

  public update(id: string, user: User): Observable<User> {
    return this.httpClient.put<User>(`${this.pathService}/${id}`, user);
  }

  public getUserSubscription(): Observable<Topic[]> {
    return this.httpClient.get<Topic[]>(`${this.pathService}/subscription`);
  }
}
