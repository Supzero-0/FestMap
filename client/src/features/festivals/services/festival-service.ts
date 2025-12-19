import { Injectable } from '@angular/core';
import { Festival, FestivalRequest } from '../types';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, switchMap, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FestivalService {
  private readonly baseUrl = '/api/festivals';
  private readonly refreshSubject = new BehaviorSubject<void>(undefined);

  constructor(private http: HttpClient) {}

  getAll$(): Observable<Festival[]> {
    return this.refreshSubject.pipe(switchMap(() => this.http.get<Festival[]>(this.baseUrl)));
  }

  create$(festival: FestivalRequest): Observable<Festival> {
    return this.http
      .post<Festival>(this.baseUrl, festival)
      .pipe(tap(() => this.refreshSubject.next()));
  }

  delete$(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}`)
      .pipe(tap(() => this.refreshSubject.next()));
  }
}
