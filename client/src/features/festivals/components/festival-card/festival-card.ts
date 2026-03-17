import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Festival } from '../../models/festival-model';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '../../../auth/services/auth-service';

@Component({
  selector: 'app-festival-card',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './festival-card.html',
  styleUrl: './festival-card.scss',
})
export class FestivalCard {
  private readonly authService = inject(AuthService);

  @Input({ required: true }) festival!: Festival;
  @Input() isSelected: boolean = false;
  @Output() selectFestival = new EventEmitter<number>();
  @Output() deleteFestival = new EventEmitter<number>();
  @Output() toggleFavorite = new EventEmitter<number>();

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  onSelect(event: Event): void {
    event.stopPropagation();
    this.selectFestival.emit(this.festival.id);
  }

  onDelete(event: Event): void {
    event.stopPropagation();
    this.deleteFestival.emit(this.festival.id);
  }

  onToggleFavorite(event: Event): void {
    event.stopPropagation();
    this.toggleFavorite.emit(this.festival.id);
  }
}
