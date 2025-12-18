import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Output } from '@angular/core';
import { Festival } from '../../types';
import { catchError, map, of, startWith } from 'rxjs';
import { ButtonModule } from 'primeng/button';
import { FestivalService } from '../../services/festival-service';

type Vm = {
  data: Festival[];
  loading: boolean;
  error: string | null;
};

@Component({
  selector: 'app-festival-list',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './festival-list.html',
  styleUrl: './festival-list.scss',
})
export class FestivalList {
  private readonly festivalService = inject(FestivalService);

  @Output() selectFestival = new EventEmitter<number>();

  readonly vm$ = this.festivalService.getAll$().pipe(
    map((data) => ({ data, loading: false, error: null }) as Vm),
    startWith({ data: [], loading: true, error: null } as Vm),
    catchError((err) => {
      console.error(err);
      return of({ data: [], loading: false, error: 'Impossible de charger les festivals.' } as Vm);
    }),
  );

  trackById = (_: number, f: Festival) => f.id;

  onSelect(id: number) {
    this.selectFestival.emit(id);
  }

  onDelete(id: number) {
    this.festivalService.delete$(id);
  }
}
