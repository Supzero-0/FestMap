import { Component, EventEmitter, Output } from '@angular/core';
import { FestivalList } from '../../../../features/festivals/components/festival-list/festival-list';

@Component({
  selector: 'app-festival-sidebar',
  standalone: true,
  imports: [FestivalList],
  templateUrl: './festival-sidebar.html',
  styleUrl: './festival-sidebar.scss',
})
export class FestivalSidebar {
  @Output() selectFestival = new EventEmitter<number>();

  onSelectFestival(festivalId: number) {
    this.selectFestival.emit(festivalId);
  }
}
