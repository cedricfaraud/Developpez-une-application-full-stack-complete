import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { DetailPostComponent } from './pages/posts/detail-post/detail-post.component';
import { NewPostComponent } from './pages/posts/new-post/new-post.component';
import { PostsComponent } from './pages/posts/posts/posts.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RegisterComponent } from './pages/register/register.component';
import { TopicsComponent } from './pages/topics/topics.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'profile',
    canActivate: [AuthGuard],
    component: ProfileComponent,
  },
  {
    path: 'topics',
    canActivate: [AuthGuard],
    component: TopicsComponent,
  },
  {
    path: 'posts',
    canActivate: [AuthGuard],
    component: PostsComponent,
  },

  {
    path: 'new-post',
    canActivate: [AuthGuard],
    component: NewPostComponent,
  },
  {
    path: 'detail-post/:id',
    canActivate: [AuthGuard],
    component: DetailPostComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
