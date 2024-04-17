import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { Topic } from 'src/app/interfaces/topic.interface';
import { TopicService } from 'src/app/services/topic.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-topic-list',
  templateUrl: './topic-list.component.html',
  styleUrl: './topic-list.component.scss',
})
export class TopicListComponent implements OnInit, OnDestroy {
  public topics$!: Observable<Topic[]>;
  @Input() fromProfilePage!: boolean;
  private unsubscribeTopicSub!: Subscription;
  private subscribeTopicSub!: Subscription;

  constructor(
    private topicService: TopicService,
    private userService: UserService
  ) {
    this.getTopics();
  }

  ngOnInit(): void {
    this.getTopics();
  }

  ngOnDestroy(): void {
    if (this.unsubscribeTopicSub !== undefined) {
      this.unsubscribeTopicSub.unsubscribe();
    }

    if (this.subscribeTopicSub !== undefined) {
      this.subscribeTopicSub.unsubscribe();
    }
  }
  public getTopics(): void {
    if (this.fromProfilePage) {
      // get topics for user subscription when on profile page
      this.topics$ = this.userService.getUserSubscription();
    } else {
      // get all topics when not on profile page
      this.topics$ = this.topicService.getAll();
    }
  }

  /**
   * Handle event when unsubscribe action is performed.
   * @param topic_id  to unsubscribe.
   */
  public onUnsubscribeTopic(topic_id: string): void {
    this.unsubscribeTopicSub = this.userService
      .unsubscribeTopic(topic_id)
      .subscribe((_) => this.getTopics());
  }

  /**
   * Handle event when subscribe action is performed.
   * @param topic_id  to subscribe.
   */
  public onSubscribeTopic(topic_id: string): void {
    this.subscribeTopicSub = this.userService
      .subscribeTopic(topic_id)
      .subscribe((_) => this.getTopics());
  }
}
