import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, finalize, shareReplay, switchMap } from 'rxjs';

import { CsrfResponse } from '../domain/auth/csrfResponse';
import { LoginResponse } from '../domain/auth/loginResponse';
import { SKIP_SESSION_REFRESH } from './auth-http-context';

@Injectable({
  providedIn: 'root',
})
export class AuthRefreshService {
  private readonly http = inject(HttpClient);
  private refreshRequest$: Observable<LoginResponse> | null = null;

  refreshSession(): Observable<LoginResponse> {
    if (!this.refreshRequest$) {
      const context = new HttpContext().set(SKIP_SESSION_REFRESH, true);

      this.refreshRequest$ = this.http.get<CsrfResponse>('/api/auth/csrf', {
        context,
        withCredentials: true,
      }).pipe(
        switchMap(() => this.http.post<LoginResponse>('/api/auth/refresh', {}, {
          context,
          withCredentials: true,
        })),
        finalize(() => {
          this.refreshRequest$ = null;
        }),
        shareReplay({ bufferSize: 1, refCount: false }),
      );
    }

    return this.refreshRequest$;
  }
}
