import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthModalService {
  private _displayModal = new BehaviorSubject<boolean>(false);
  displayModal$ = this._displayModal.asObservable();

  private _activeTab = new BehaviorSubject<number>(0); // 0 for login, 1 for register
  activeTab$ = this._activeTab.asObservable();

  constructor() {}

  openModal(tabIndex: number = 0) {
    this._activeTab.next(tabIndex);
    this._displayModal.next(true);
  }

  closeModal() {
    this._displayModal.next(false);
  }

  setActiveTab(tabIndex: number) {
    this._activeTab.next(tabIndex);
  }
}
