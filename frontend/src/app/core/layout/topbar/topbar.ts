import { Component, inject, input, output } from '@angular/core';
import { AuthService } from '../../services/auth';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { CurrentUserResponse } from '../../domain/auth/currentUserResponse';

@Component({
  selector: 'app-topbar',
  imports: [],
  templateUrl: './topbar.html',
  styleUrl: './topbar.css',
})
export class Topbar {
  readonly sidebarCollapsed = input(false);
  readonly sidebarToggle = output<void>();

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  currentUser: CurrentUserResponse | null = null;

  constructor() {
    this.authService.me().subscribe({
      next: (user) => {
        this.currentUser = user;
        console.log('Current user:', user);
      },
      error: (error) => {
        console.error('/me failed:', error);
      },
    });
  }

  logout(){
    Swal.fire({
      title: 'Log out?',
      text: 'You will be redirected to the login page.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Logout',
      cancelButtonText: 'Cancel',
      confirmButtonColor: '#f15153',
      cancelButtonColor: '#321847',
    }).then((result) => {
      if (result.isConfirmed) {
        this.authService.logout().pipe(
          switchMap(() => this.router.navigate(['/login']))
        ).subscribe();
      }
    })
  }
}
