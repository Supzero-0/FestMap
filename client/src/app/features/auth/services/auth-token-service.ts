import { Injectable } from '@angular/core';
import { Role } from '../models/user-model';

@Injectable({
  providedIn: 'root',
})
export class AuthTokenService {
  private readonly TOKEN_KEY = 'access_token';
  private readonly ROLE_KEY = 'user_role';
  private readonly EMAIL_KEY = 'user_email';

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getRole(): Role | null {
    return localStorage.getItem(this.ROLE_KEY) as Role | null;
  }

  setRole(role: Role): void {
    localStorage.setItem(this.ROLE_KEY, role);
  }

  getEmail(): string | null {
    return localStorage.getItem(this.EMAIL_KEY);
  }

  setEmail(email: string): void {
    localStorage.setItem(this.EMAIL_KEY, email);
  }

  clearToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.ROLE_KEY);
    localStorage.removeItem(this.EMAIL_KEY);
  }
}
