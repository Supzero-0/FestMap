import { Component, ViewChild } from '@angular/core';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AuthModalComponent } from './features/auth/components/auth-modal/auth-modal';
import { SidebarComponent } from './shared/components/sidebar/sidebar';
import { FestivalList } from './features/festivals/components/festival-list/festival-list';
import { FestivalMap } from './features/festivals/components/festival-map/festival-map';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ToastModule, AuthModalComponent, SidebarComponent, FestivalList, FestivalMap],
  templateUrl: './app.html',
  styleUrl: './app.scss',
  providers: [MessageService],
})
export class App {
  @ViewChild(FestivalMap) festivalMap!: FestivalMap;

  constructor() {}

  onSelectFestival(id: number): void {
    this.festivalMap.focusMarker(id);
  }
}
