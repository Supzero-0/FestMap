import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { catchError, finalize, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HealthService {
  private _loading = signal(false);
  private _result = signal<any>(null);
  private _error = signal<string | null>(null);

  loading = this._loading.asReadonly();
  result = this._result.asReadonly();
  error = this._error.asReadonly();

  constructor(private http: HttpClient) {}

  checkHealth() {
    this._loading.set(true);
    this._result.set(null);
    this._error.set(null);

    return this.http.get<any>('/api/health')
      .pipe(
        catchError(error => {
          this._error.set(`Erreur API: ${error.message || error.status}`);
          return of(null);
        }),
        finalize(() => this._loading.set(false))
      )
      .subscribe({
        next: (data) => {
          if (data) {
            this._result.set(data);
          }
        }
      });
  }
}
