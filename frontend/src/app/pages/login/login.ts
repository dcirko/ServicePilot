import { HttpErrorResponse } from '@angular/common/http';
import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private readonly authService = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly isSubmitting = signal(false);
  readonly submitted = signal(false);
  readonly errorMessage = signal('');

  readonly loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  readonly email = this.loginForm.controls.email;
  readonly password = this.loginForm.controls.password;

  onSubmit(): void {
    this.submitted.set(true);
    this.errorMessage.set('');

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);

    console.log(this.loginForm.getRawValue());

    this.authService.login(this.loginForm.getRawValue())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response) => {
          console.log('Login successful', response.user);
          this.router.navigate(['/home']);
        },
        error: (error) => {
          console.error('Login failed', error);
          this.errorMessage.set(this.resolveErrorMessage(error));
          this.isSubmitting.set(false);
        },
        complete: () => {
          this.isSubmitting.set(false);
        },
      });
  }

  showEmailError(): boolean {
    return this.submitted() && this.email.invalid;
  }

  showPasswordError(): boolean {
    return this.submitted() && this.password.invalid;
  }

  private resolveErrorMessage(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      if (error.status === 0) {
        return 'Ne mogu se spojiti na ServicePilot. Pokusaj ponovno za nekoliko trenutaka.';
      }

      if (typeof error.error?.message === 'string') {
        return error.error.message;
      }

      if (error.status === 401) {
        return 'Invalid email or password.';
      }
    }

    return 'Neispravan email ili lozinka.';
  }
}
