import { Injectable } from '@angular/core';
import { Festival, FestivalApi } from '../types';
import { delay, Observable, of } from 'rxjs';
import festivalsData from '../data/mockFestivals.json';

@Injectable({
  providedIn: 'root',
})
export class MockFestivalService implements FestivalApi {
  getAll$(): Observable<Festival[]> {
    // micro délai pour simuler une latence réseau et déclencher le pipe async
    return of(structuredClone(festivalsData) as Festival[]).pipe(delay(0));
  }
}
