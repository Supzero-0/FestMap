import { Injectable } from '@angular/core';
import { Festival, FestivalApi } from '../types';
import { BehaviorSubject, delay, Observable, of } from 'rxjs';
import festivalsData from '../data/mockFestivals.json';

@Injectable({
  providedIn: 'root',
})
export class MockFestivalService implements FestivalApi {
  private readonly store$ = new BehaviorSubject<Festival[]>(
    structuredClone(festivalsData) as Festival[],
  );

  getAll$(): Observable<Festival[]> {
    // micro délai pour simuler une latence réseau et déclencher le pipe async
    return this.store$.asObservable().pipe(delay(0));
  }

  delete$(id: number): Observable<void> {
    const next = this.store$.value.filter((f) => f.id !== id);
    this.store$.next(next);
    // Réponse simulé
    return of(void 0).pipe(delay(0));
  }
}
