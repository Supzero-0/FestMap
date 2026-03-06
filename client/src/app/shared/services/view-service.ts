import { Injectable, signal, inject } from '@angular/core';
import { FestivalService } from '../../features/festivals/services/festival-service';

export type MainView = 'map' | 'admin' | 'favorites';

@Injectable({
  providedIn: 'root',
})
export class ViewService {
  private readonly festivalService = inject(FestivalService);
  private readonly currentView = signal<MainView>('map');

  readonly view = this.currentView.asReadonly();

  setView(view: MainView): void {
    this.currentView.set(view);

    if (view === 'favorites') {
      this.festivalService.setFavoritesOnly(true);
    } else {
      this.festivalService.setFavoritesOnly(false);
    }
  }
}
