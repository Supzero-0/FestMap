import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User } from '../models/user-model';
import { AuthResponse } from '../models/auth-response-model';
import { AuthTokenService } from './auth-token-service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private authTokenService: AuthTokenService,
  ) {}

  login(credentials: User): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/login`, credentials)
      .pipe(tap((response) => this.authTokenService.setToken(response.token)));
  }

  register(credentials: User): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/register`, credentials)
      .pipe(tap((response) => this.authTokenService.setToken(response.token)));
  }

  logout(): void {
    this.authTokenService.clearToken();
  }

  isAuthenticated(): boolean {
    return !!this.authTokenService.getToken();
  }
}
