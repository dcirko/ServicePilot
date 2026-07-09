import { HttpErrorResponse, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';

import { AuthRefreshService } from './auth-refresh.service';
import { SESSION_RETRY_ATTEMPT, SKIP_SESSION_REFRESH } from './auth-http-context';

const REFRESH_EXCLUDED_PATHS = [
  '/api/auth/csrf',
  '/api/auth/login',
  '/api/auth/register',
  '/api/auth/refresh',
];

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const router = inject(Router);
  const authRefreshService = inject(AuthRefreshService);
  const apiRequest = isApiRequest(request)
    ? request.clone({ withCredentials: true })
    : request;

  if (!isApiRequest(apiRequest) || apiRequest.context.get(SKIP_SESSION_REFRESH)) {
    return next(apiRequest);
  }

  return next(apiRequest).pipe(
    catchError((error: unknown) => {
      if (!shouldRefreshSession(error, apiRequest)) {
        return throwError(() => error);
      }

      const retryRequest = apiRequest.clone({
        context: apiRequest.context.set(SESSION_RETRY_ATTEMPT, true),
        withCredentials: true,
      });

      return authRefreshService.refreshSession().pipe(
        switchMap(() => next(retryRequest)),
        catchError((refreshError: unknown) => {
          router.navigate(['/login']);
          return throwError(() => refreshError);
        }),
      );
    }),
  );
};

function shouldRefreshSession(error: unknown, request: HttpRequest<unknown>): error is HttpErrorResponse {
  return error instanceof HttpErrorResponse
    && error.status === 401
    && !request.context.get(SESSION_RETRY_ATTEMPT)
    && !isRefreshExcluded(request);
}

function isApiRequest(request: HttpRequest<unknown>): boolean {
  return request.url.startsWith('/api/');
}

function isRefreshExcluded(request: HttpRequest<unknown>): boolean {
  const path = request.url.split(/[?#]/, 1)[0];
  return REFRESH_EXCLUDED_PATHS.includes(path);
}
