import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Festival, FESTIVAL_API } from '../../types';
import { catchError, map, of, startWith } from 'rxjs';

type Vm = {
  data: Festival[];
  loading: boolean;
  error: string | null;
};

@Component({
  selector: 'app-festival-list',
  imports: [CommonModule],
  templateUrl: './festival-list.html',
  styleUrl: './festival-list.scss',
})
export class FestivalList {
  private readonly api = inject(FESTIVAL_API);

  readonly vm$ = this.api.getAll$().pipe(
    map((data) => ({ data, loading: false, error: null }) as Vm),
    startWith({ data: [], loading: true, error: null } as Vm),
    catchError((err) => {
      console.error(err);
      return of({ data: [], loading: false, error: 'Impossible de charger les festivals.' } as Vm);
    }),
  );

  trackById = (_: number, f: Festival) => f.id;
}
