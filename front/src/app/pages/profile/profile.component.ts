import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
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
    email: [
      '',
      [
        Validators.email,
        Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$'),
      ],
    ],
    name: ['', [Validators.min(3)]],
    password: [
      '',
      [
        Validators.pattern(
          '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.-_*])([a-zA-Z0-9@#$%^&+=*.-_]){8,}$'
        ), // only with a number, a lowercase, a uppercase, a special character and 8 size
      ],
    ],
  });
  constructor(
    private router: Router,
    private authService: AuthService,
    private fb: FormBuilder,
    private sessionService: SessionService,
    private matSnackBar: MatSnackBar
  ) {}
  ngOnInit(): void {
    this.authService.me().subscribe((user: User) => (this.user = user));
  }
  public back(): void {
    window.history.back();
  }

  hide = true;

  public submit(): void {
    let registerRequest = this.form.value as UpdateRequest;

    this.authService.update(registerRequest).subscribe(
      (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        this.authService.me().subscribe((user: User) => {
          this.sessionService.logIn(user);
          this.router.navigate(['profile']);
          this.matSnackBar.open('Informations mise à jour!', 'Close', {
            duration: 3000,
          });
        });
      },
      (error) => (this.onError = true)
    );
  }
  logout(): void {
    this.sessionService.logOut();
    this.router.navigateByUrl('');
  }
}
