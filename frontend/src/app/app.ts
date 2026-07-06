import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';

import { Footer } from './core/layout/footer/footer';
import { Sidebar } from './core/layout/sidebar/sidebar';
import { Topbar } from './core/layout/topbar/topbar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Sidebar, Topbar, Footer],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly title = signal('frontend');
  readonly isAuthPage = signal(true);
  readonly sidebarCollapsed = signal(false);

  constructor() {
    this.updateShellVisibility(this.router.url);

    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((event) => this.updateShellVisibility(event.urlAfterRedirects));
  }

  toggleSidebar(): void {
    this.sidebarCollapsed.update((collapsed) => !collapsed);
  }

  private updateShellVisibility(url: string): void {
    const path = url.split(/[?#]/, 1)[0];
    const isAuthRoute =
      path === '/' ||
      path === '/login' ||
      path.startsWith('/login/') ||
      path === '/register' ||
      path.startsWith('/register/');

    this.isAuthPage.set(isAuthRoute);
  }
}
