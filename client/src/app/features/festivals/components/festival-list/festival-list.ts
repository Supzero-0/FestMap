import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  inject,
  Output,
  OnInit,
  OnDestroy,
  ChangeDetectorRef,
} from '@angular/core';
import { Festival } from '../../types';
import { catchError, finalize, map, of, Subscription, switchMap } from 'rxjs';
import { ButtonModule } from 'primeng/button';
import { FestivalService } from '../../services/festival-service';
import { FestivalCard } from '../festival-card/festival-card';
import { FestivalSelection } from '../../services/festival-selection';

@Component({
  selector: 'app-festival-list',
  standalone: true,
  imports: [CommonModule, ButtonModule, FestivalCard],
  templateUrl: './festival-list.html',
  styleUrl: './festival-list.scss',
})
export class FestivalList implements OnInit, OnDestroy {
  private readonly festivalService = inject(FestivalService);
  private readonly festivalSelection = inject(FestivalSelection);
  private readonly cdr = inject(ChangeDetectorRef);
  private festivalSubscription: Subscription | undefined;
  private selectedFestivalIdSubscription: Subscription | undefined;

  @Output() selectFestival = new EventEmitter<number>();

  festivals: Festival[] = [];
  originalFestivalsOrder: Festival[] = [];
  selectedFestivalId: number | null = null;
  loading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.loading = true;
    this.error = null;

    this.festivalSubscription = this.festivalService
      .getAll$()
      .pipe(
        map((data) => {
          this.originalFestivalsOrder = [...data];
          return data;
        }),
        catchError((err) => {
          console.error(err);
          this.error = 'Impossible de charger les festivals.';
          this.loading = false;
          this.cdr.detectChanges();
          return of([] as Festival[]);
        }),
      )
      .subscribe((data) => {
        this.festivals = data;
        this.reorderFestivals();
        this.loading = false;
        this.cdr.detectChanges();
      });

    this.selectedFestivalIdSubscription = this.festivalSelection.selectedFestivalId$.subscribe(
      (id) => {
        this.selectedFestivalId = id;
        this.reorderFestivals();
        this.cdr.detectChanges();
      },
    );
  }

  ngOnDestroy(): void {
    this.festivalSubscription?.unsubscribe();
    this.selectedFestivalIdSubscription?.unsubscribe();
  }

  reorderFestivals(): void {
    if (this.selectedFestivalId === null) {
      this.festivals = [...this.originalFestivalsOrder];
      return;
    }

    const selected = this.originalFestivalsOrder.find((f) => f.id === this.selectedFestivalId);
    if (selected) {
      const otherFestivals = this.originalFestivalsOrder.filter(
        (f) => f.id !== this.selectedFestivalId,
      );
      this.festivals = [selected, ...otherFestivals];
    }
  }

  trackById = (_: number, f: Festival) => f.id;

  onSelect(id: number) {
    this.selectFestival.emit(id);
    this.festivalSelection.selectFestival(id);
  }

  onDelete(id: number) {
    this.loading = true;
    this.error = null;

    this.festivalService
      .delete$(id)
      .pipe(
        switchMap(() => this.festivalService.getAll$()),
        map((data) => {
          this.originalFestivalsOrder = [...data];
          return data;
        }),
        catchError((err) => {
          console.error(err);
          this.error = 'Impossible de supprimer le festival.';
          return of(this.festivals);
        }),
        finalize(() => {
          this.loading = false;
        }),
      )
      .subscribe((data) => {
        this.festivals = data;
        this.reorderFestivals();
      });
  }

  onToggleFavorite(id: number) {
    console.log(`Toggle favorite for festival with ID: ${id}`);
    // Implement actual favorite toggling logic here
  }
}
