import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Festival } from '../../types';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-festival-card',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './festival-card.html',
  styleUrl: './festival-card.scss',
})
export class FestivalCard {
  @Input({ required: true }) festival!: Festival;
  @Input() isSelected: boolean = false;
  @Output() selectFestival = new EventEmitter<number>();
  @Output() deleteFestival = new EventEmitter<number>();
  @Output() toggleFavorite = new EventEmitter<number>();

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
