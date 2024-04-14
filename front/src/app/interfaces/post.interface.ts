import { PostComment } from './comment.interface';
import { Topic } from './topic.interface';
import { User } from './user.interface';

export interface Post {
  id: number;
  title: string;
  createdAt: Date;
  user: User;
  content: string;
  topic: Topic;
  comments: PostComment[];
}
