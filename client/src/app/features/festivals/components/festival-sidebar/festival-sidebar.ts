import {
  Component,
  EventEmitter,
  Output,
  ViewChild,
  ElementRef,
  inject,
  OnInit,
  DestroyRef,
} from '@angular/core';
import { FestivalList } from '../../../../features/festivals/components/festival-list/festival-list';
import { FestivalSelection } from '../../services/festival-selection';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-festival-sidebar',
  standalone: true,
  imports: [FestivalList],
  templateUrl: './festival-sidebar.html',
  styleUrl: './festival-sidebar.scss',
})
export class FestivalSidebar implements OnInit {
  private readonly festivalSelection = inject(FestivalSelection);
  private readonly destroyRef = inject(DestroyRef);

  @ViewChild('scrollContainer') scrollContainer!: ElementRef<HTMLDivElement>;
  @Output() selectFestival = new EventEmitter<number>();

  ngOnInit(): void {
    this.festivalSelection.selectedFestivalId$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((id) => {
        if (id !== null) {
          this.scrollToTop();
        }
      });
  }

  onSelectFestival(festivalId: number) {
    this.selectFestival.emit(festivalId);
  }

  private scrollToTop(): void {
    if (this.scrollContainer) {
      setTimeout(() => {
        this.scrollContainer.nativeElement.scrollTo({
          top: 0,
          behavior: 'smooth',
        });
      }, 50);
    }
  }
}
