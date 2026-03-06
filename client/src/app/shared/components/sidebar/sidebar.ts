import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '../../../features/auth/services/auth-service';
import { AuthModalService } from '../../../features/auth/services/auth-modal';
import { ViewService, MainView } from '../../services/view-service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.scss'],
})
export class SidebarComponent {
  private readonly authService = inject(AuthService);
  private readonly authModalService = inject(AuthModalService);
  private readonly viewService = inject(ViewService);

  readonly currentView = this.viewService.view;

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  getUserEmail(): string | null {
    return this.authService.getUserEmail();
  }

  logout(): void {
    this.authService.logout();
    this.viewService.setView('map');
  }

  openLoginModal(): void {
    this.authModalService.openModal(0);
  }

  setMainView(view: MainView): void {
    if (view === 'favorites' && !this.isAuthenticated()) {
      this.openLoginModal();
      return;
    }
    this.viewService.setView(view);
  }
}
