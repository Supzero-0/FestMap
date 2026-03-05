import { Injectable, signal } from '@angular/core';

export type MainView = 'map' | 'admin';

@Injectable({
  providedIn: 'root',
})
export class ViewService {
  private readonly currentView = signal<MainView>('map');

  readonly view = this.currentView.asReadonly();

  setView(view: MainView): void {
    this.currentView.set(view);
  }
}
