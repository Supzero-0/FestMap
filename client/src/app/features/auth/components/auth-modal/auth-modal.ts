import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { TabsModule } from 'primeng/tabs';
import { ReactiveFormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';

import { AuthModalService } from '../../services/auth-modal';
import { LoginFormComponent } from '../login-form/login-form';
import { RegisterFormComponent } from '../register-form/register-form';

@Component({
  selector: 'app-auth-modal',
  standalone: true,
  imports: [
    CommonModule,
    DialogModule,
    ButtonModule,
    TabsModule,
    ReactiveFormsModule,
    LoginFormComponent,
    RegisterFormComponent,
  ],
  templateUrl: './auth-modal.html',
  styleUrls: ['./auth-modal.scss'],
})
export class AuthModalComponent implements OnInit, OnDestroy {
  @ViewChild(LoginFormComponent) loginFormComponent!: LoginFormComponent;
  @ViewChild(RegisterFormComponent) registerFormComponent!: RegisterFormComponent;

  display = false;
  activeIndex = 0; // 0 login, 1 register
  private subscriptions = new Subscription();

  constructor(private authModalService: AuthModalService) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.authModalService.displayModal$.subscribe((display) => (this.display = display)),
    );

    this.subscriptions.add(
      this.authModalService.activeTab$.subscribe((index) => (this.activeIndex = index)),
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  onTabValueChange(value: string | number | undefined): void {
    if (value === undefined || value === null) {
      return;
    }

    const numericValue = typeof value === 'string' ? Number(value) : value;

    this.activeIndex = numericValue;
    this.authModalService.setActiveTab(numericValue);
  }

  submitForm(): void {
    if (this.activeIndex === 0 && this.loginFormComponent) {
      this.loginFormComponent.onSubmit();
    } else if (this.activeIndex === 1 && this.registerFormComponent) {
      this.registerFormComponent.onSubmit();
    }
  }

  onHide() {
    this.authModalService.closeModal();
  }
}
