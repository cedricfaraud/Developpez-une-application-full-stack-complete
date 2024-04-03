import { User } from './user.interface';

export interface PostComment {
  id: string;
  date: Date;
  content: string;
  user: User;
}
