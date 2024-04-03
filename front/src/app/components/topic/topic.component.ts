import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Topic } from 'src/app/interfaces/topic.interface';

@Component({
  selector: 'app-topic',
  templateUrl: './topic.component.html',
  styleUrl: './topic.component.scss',
})
export class TopicComponent {
  @Input() fromProfilePage!: boolean;
  @Input() topic!: Topic;

  @Output() unsubscribeTopic = new EventEmitter<string>();
  @Output() subscribeTopic = new EventEmitter<string>();

  public unsetSubscribeTopic(topic: Topic): void {
    if (topic) {
      this.unsubscribeTopic.emit(topic.id.toString());
    }
  }
  public setSubscribeTopic(topic: Topic): void {
    if (topic) {
      this.subscribeTopic.emit(topic.id.toString());
    }
  }
}
