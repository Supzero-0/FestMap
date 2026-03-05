import { Injectable } from '@angular/core';
import { Festival, FestivalRequest } from '../models/festival-model';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, combineLatest, map, switchMap, tap, shareReplay } from 'rxjs';
import { environment } from '../../../../environments/environment';

export type DateFilter = 'all' | 'this-month' | 'this-summer' | 'this-year';

@Injectable({
  providedIn: 'root',
})
export class FestivalService {
  private readonly baseUrl = `${environment.apiUrl}/festivals`;
  private readonly refreshSubject = new BehaviorSubject<void>(undefined);
  private readonly searchSubject = new BehaviorSubject<string>('');
  private readonly genreSubject = new BehaviorSubject<string | null>(null);
  private readonly countrySubject = new BehaviorSubject<string | null>(null);
  private readonly dateFilterSubject = new BehaviorSubject<DateFilter>('all');

  constructor(private http: HttpClient) {}

  getAll$(): Observable<Festival[]> {
    return this.refreshSubject.pipe(
      switchMap(() => this.http.get<Festival[]>(this.baseUrl)),
      shareReplay(1),
    );
  }

  getFiltered$(): Observable<Festival[]> {
    return combineLatest([
      this.getAll$(),
      this.searchSubject.asObservable(),
      this.genreSubject.asObservable(),
      this.countrySubject.asObservable(),
      this.dateFilterSubject.asObservable(),
    ]).pipe(
      map(([festivals, search, genre, country, dateFilter]) =>
        this.filterFestivals(festivals, search, genre, country, dateFilter),
      ),
    );
  }

  setSearchQuery(query: string): void {
    this.searchSubject.next(query);
  }

  setGenre(genre: string | null): void {
    this.genreSubject.next(genre);
  }

  setCountry(country: string | null): void {
    this.countrySubject.next(country);
  }

  setDateFilter(filter: DateFilter): void {
    this.dateFilterSubject.next(filter);
  }

  getFilters$() {
    return combineLatest({
      genre: this.genreSubject.asObservable(),
      country: this.countrySubject.asObservable(),
      dateFilter: this.dateFilterSubject.asObservable(),
    });
  }

  private filterFestivals(
    festivals: Festival[],
    search: string,
    genre: string | null,
    country: string | null,
    dateFilter: DateFilter,
  ): Festival[] {
    let filtered = festivals;

    // Search filter
    const s = search.toLowerCase().trim();
    if (s) {
      filtered = filtered.filter((f) => {
        const nameMatch = f.name.toLowerCase().includes(s);
        const cityMatch = f.address.city.toLowerCase().includes(s);
        const countryMatch = f.address.country.toLowerCase().includes(s);
        const genreMatch = f.genre?.toLowerCase().includes(s);
        const descriptionMatch = f.description?.toLowerCase().includes(s);
        return nameMatch || cityMatch || countryMatch || genreMatch || descriptionMatch;
      });
    }

    // Genre filter
    if (genre) {
      filtered = filtered.filter((f) => f.genre === genre);
    }

    // Country filter
    if (country) {
      filtered = filtered.filter((f) => f.address.country === country);
    }

    // Date filter
    if (dateFilter !== 'all') {
      const now = new Date();
      filtered = filtered.filter((f) => {
        const startDate = new Date(f.startDate);

        if (dateFilter === 'this-month') {
          return (
            startDate.getMonth() === now.getMonth() && startDate.getFullYear() === now.getFullYear()
          );
        }

        if (dateFilter === 'this-summer') {
          const month = startDate.getMonth(); // 0-indexed: 5=June, 6=July, 7=August, 8=September
          return month >= 5 && month <= 8 && startDate.getFullYear() === now.getFullYear();
        }

        if (dateFilter === 'this-year') {
          return startDate.getFullYear() === now.getFullYear();
        }

        return true;
      });
    }

    return filtered;
  }

  create$(festival: FestivalRequest): Observable<Festival> {
    return this.http
      .post<Festival>(this.baseUrl, festival)
      .pipe(tap(() => this.refreshSubject.next()));
  }

  update$(id: number, festival: FestivalRequest): Observable<Festival> {
    return this.http
      .put<Festival>(`${this.baseUrl}/${id}`, festival)
      .pipe(tap(() => this.refreshSubject.next()));
  }

  delete$(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}`)
      .pipe(tap(() => this.refreshSubject.next()));
  }
}
