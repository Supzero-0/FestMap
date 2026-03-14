import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FestivalSelection {
  private _selectedFestivalId = new BehaviorSubject<number | null>(null);
  selectedFestivalId$: Observable<number | null> = this._selectedFestivalId.asObservable();

  get selectedFestivalId(): number | null {
    return this._selectedFestivalId.getValue();
  }

  selectFestival(id: number | null): void {
    this._selectedFestivalId.next(id);
  }
}
