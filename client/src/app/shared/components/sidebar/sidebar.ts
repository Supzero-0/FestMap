import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '../../../features/auth/services/auth-service';
import { AuthModalService } from '../../../features/auth/services/auth-modal';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.scss'],
})
export class SidebarComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private authModalService: AuthModalService,
  ) {}

  ngOnInit(): void {}

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  getUserEmail(): string | null {
    if (this.isAuthenticated()) {
      return 'user@example.com';
    }
    return null;
  }

  logout(): void {
    this.authService.logout();
  }

  openLoginModal(): void {
    this.authModalService.openModal(0); // 0 for login tab
  }

  openRegisterModal(): void {
    this.authModalService.openModal(1); // 1 for register tab
  }
}
