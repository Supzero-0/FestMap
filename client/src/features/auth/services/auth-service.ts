import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from './../../../environments/environment';
import { Role, User } from '../models/user-model';
import { AuthResponse } from '../models/auth-response-model';
import { AuthTokenService } from './auth-token-service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly authTokenService = inject(AuthTokenService);
  private readonly apiUrl = environment.apiUrl;

  login(credentials: User): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap((response) => {
        this.authTokenService.setToken(response.token);
        this.authTokenService.setRole(response.role);
        this.authTokenService.setEmail(response.email);
      }),
    );
  }

  register(credentials: User): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, credentials).pipe(
      tap((response) => {
        this.authTokenService.setToken(response.token);
        this.authTokenService.setRole(response.role);
        this.authTokenService.setEmail(response.email);
      }),
    );
  }

  logout(): void {
    this.authTokenService.clearToken();
  }

  isAuthenticated(): boolean {
    return !!this.authTokenService.getToken();
  }

  isAdmin(): boolean {
    return this.authTokenService.getRole() === 'ADMIN';
  }

  getRole(): Role | null {
    return this.authTokenService.getRole();
  }

  getUserEmail(): string | null {
    return this.authTokenService.getEmail();
  }
}
