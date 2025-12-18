import { Injectable } from '@angular/core';
import { Festival, FestivalRequest } from '../types';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FestivalService {
  private readonly baseUrl = '/api/festivals';

  constructor(private http: HttpClient) {}

  getAll$(): Observable<Festival[]> {
    return this.http.get<Festival[]>(this.baseUrl);
  }

  create$(festival: FestivalRequest): Observable<Festival> {
    return this.http.post<Festival>(this.baseUrl, festival);
  }

  delete$(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
