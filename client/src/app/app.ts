import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AuthModalComponent } from '../features/auth/components/auth-modal/auth-modal';
import { SidebarComponent } from '../shared/components/sidebar/sidebar';
import { FestivalMap } from '../features/festivals/components/festival-map/festival-map';
import { FestivalSidebar } from '../features/festivals/components/festival-sidebar/festival-sidebar';
import { ViewService } from '../shared/services/view-service';
import { AdminPage } from '../features/admin/admin-page/admin-page';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    ToastModule,
    AuthModalComponent,
    SidebarComponent,
    FestivalSidebar,
    FestivalMap,
    AdminPage,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss',
  providers: [MessageService],
})
export class App {
  private readonly viewService = inject(ViewService);
  readonly currentView = this.viewService.view;
}
