import { Injectable } from '@angular/core';
import { Festival, FestivalRequest } from '../types';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, combineLatest, map, switchMap, tap, shareReplay } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FestivalService {
  private readonly baseUrl = `${environment.apiUrl}/festivals`;
  private readonly refreshSubject = new BehaviorSubject<void>(undefined);
  private readonly searchSubject = new BehaviorSubject<string>('');

  constructor(private http: HttpClient) {}

  getAll$(): Observable<Festival[]> {
    return this.refreshSubject.pipe(
      switchMap(() => this.http.get<Festival[]>(this.baseUrl)),
      shareReplay(1),
    );
  }

  getFiltered$(): Observable<Festival[]> {
    return combineLatest([this.getAll$(), this.searchSubject.asObservable()]).pipe(
      map(([festivals, search]) => this.filterFestivals(festivals, search)),
    );
  }

  setSearchQuery(query: string): void {
    this.searchSubject.next(query);
  }

  private filterFestivals(festivals: Festival[], search: string): Festival[] {
    const s = search.toLowerCase().trim();
    if (!s) return festivals;

    return festivals.filter((f) => {
      const nameMatch = f.name.toLowerCase().includes(s);
      const cityMatch = f.address.city.toLowerCase().includes(s);
      const countryMatch = f.address.country.toLowerCase().includes(s);
      const genreMatch = f.genre?.toLowerCase().includes(s);
      const descriptionMatch = f.description?.toLowerCase().includes(s);

      return nameMatch || cityMatch || countryMatch || genreMatch || descriptionMatch;
    });
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
