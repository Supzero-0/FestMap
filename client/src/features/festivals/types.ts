import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';

export interface Festival {
  id: number;
  name: string;
  city: string;
  lat: number;
  lng: number;
  date?: string;
  genre?: string;
}

export interface FestivalApi {
  getAll$(): Observable<Festival[]>;
}

export const FESTIVAL_API = new InjectionToken<FestivalApi>('FESTIVAL_API');
