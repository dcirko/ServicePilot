import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import { LoginRequest } from '../domain/auth/loginRequest';
import { LoginResponse } from '../domain/auth/loginResponse';
import { LoginUser } from '../domain/auth/loginUser';
import { CsrfResponse } from '../domain/auth/csrfResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/auth';
  private readonly httpOptions = {
    withCredentials: true,
  } as const;

  csrf(): Observable<CsrfResponse> {
    return this.http.get<CsrfResponse>(`${this.apiUrl}/csrf`, this.httpOptions);
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.csrf().pipe(
      switchMap(() => this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials, this.httpOptions))
    );
  }

}
