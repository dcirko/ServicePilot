import { HttpErrorResponse } from '@angular/common/http';
import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private readonly authService = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly isSubmitting = signal(false);
  readonly submitted = signal(false);
  readonly errorMessage = signal('');

  readonly registerForm = this.fb.nonNullable.group({
    firstName: ['', [Validators.required, Validators.maxLength(100)]],
    lastName: ['', [Validators.required, Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(150)]],
    password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(100)]],
    phone: ['', [Validators.maxLength(30)]],
  });

  readonly firstName = this.registerForm.controls.firstName;
  readonly lastName = this.registerForm.controls.lastName;
  readonly email = this.registerForm.controls.email;
  readonly password = this.registerForm.controls.password;
  readonly phone = this.registerForm.controls.phone;

  onSubmit(): void {
    this.submitted.set(true);
    this.errorMessage.set('');

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);

    this.authService.register(this.registerForm.getRawValue())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Register failed', error);
          this.errorMessage.set(this.resolveErrorMessage(error));
          this.isSubmitting.set(false);
        },
        complete: () => {
          this.isSubmitting.set(false);
        },
      });
  }

  showFirstNameError(): boolean {
    return this.submitted() && this.firstName.invalid;
  }

  showLastNameError(): boolean {
    return this.submitted() && this.lastName.invalid;
  }

  showEmailError(): boolean {
    return this.submitted() && this.email.invalid;
  }

  showPhoneError(): boolean {
    return this.submitted() && this.phone.invalid;
  }

  showPasswordError(): boolean {
    return this.submitted() && this.password.invalid;
  }

  private resolveErrorMessage(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      if (error.status === 0) {
        return 'Unable to connect to ServicePilot.';
      }

      if (typeof error.error?.message === 'string') {
        return error.error.message;
      }
    }

    return 'Registration failed. Please try again.';
  }
}
