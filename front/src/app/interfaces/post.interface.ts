import { Topic } from './topic.interface';
import { User } from './user.interface';

export interface Post {
  id: number;
  title: string;
  date: Date;
  user: User;
  content: string;
  topic: Topic;
  comments: Comment[];
}
