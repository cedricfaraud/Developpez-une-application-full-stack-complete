import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthSuccess } from 'src/app/interfaces/authSuccess.interface';
import { UpdateRequest } from 'src/app/interfaces/updateRequest.interface';
import { User } from 'src/app/interfaces/user.interface';
import { AuthService } from 'src/app/services/auth.service';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  public user: User | undefined;
  public onError = false;

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    name: ['', [Validators.required, Validators.min(3)]],
    password: ['', [Validators.required, Validators.min(3)]],
  });
  constructor(
    private router: Router,
    private authService: AuthService,
    private fb: FormBuilder,
    private sessionService: SessionService
  ) {}
  ngOnInit(): void {
    this.authService.me().subscribe((user: User) => (this.user = user));
  }
  public back() {
    window.history.back();
  }

  hide = true;

  public submit(): void {
    const registerRequest = this.form.value as UpdateRequest;
    this.authService.update(registerRequest).subscribe(
      (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        this.authService.me().subscribe((user: User) => {
          this.sessionService.logIn(user);
          this.router.navigate(['profile']);
        });
      },
      (error) => (this.onError = true)
    );
  }
  logout() {
    this.sessionService.logOut();
    this.router.navigateByUrl('/login');
  }
}
