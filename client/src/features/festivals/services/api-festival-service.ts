import { Injectable } from '@angular/core';
import { Festival, FestivalApi } from '../types';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiFestivalService implements FestivalApi {
  private readonly baseUrl = '/api/festivals';

  constructor(private http: HttpClient) {}

  getAll$(): Observable<Festival[]> {
    return this.http.get<Festival[]>(this.baseUrl);
  }
}
