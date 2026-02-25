import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { AuthModalComponent } from './features/auth/components/auth-modal/auth-modal';
import { AuthModalService } from './features/auth/services/auth-modal';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastModule, ButtonModule, AuthModalComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
  providers: [MessageService, AuthModalService],
})
export class App {
  constructor(private authModalService: AuthModalService) {}

  openLoginModal() {
    this.authModalService.openModal(0);
  }

  openRegisterModal() {
    this.authModalService.openModal(1);
  }
}
