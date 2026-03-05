import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, switchMap, tap, shareReplay } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { User } from '../../auth/models/user-model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/admin/users`;
  private readonly refreshSubject = new BehaviorSubject<void>(undefined);

  getAll$(): Observable<User[]> {
    return this.refreshSubject.pipe(
      switchMap(() => this.http.get<User[]>(this.baseUrl)),
      shareReplay(1),
    );
  }

  getById$(id: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }

  create$(user: User): Observable<User> {
    return this.http.post<User>(this.baseUrl, user).pipe(tap(() => this.refreshSubject.next()));
  }

  update$(id: string, user: User): Observable<User> {
    return this.http
      .put<User>(`${this.baseUrl}/${id}`, user)
      .pipe(tap(() => this.refreshSubject.next()));
  }

  delete$(id: string): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}`)
      .pipe(tap(() => this.refreshSubject.next()));
  }
}
