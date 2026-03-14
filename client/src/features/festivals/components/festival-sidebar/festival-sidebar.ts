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
import { FestivalService } from '../../services/festival-service';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';

@Component({
  selector: 'app-festival-sidebar',
  standalone: true,
  imports: [FestivalList],
  templateUrl: './festival-sidebar.html',
  styleUrl: './festival-sidebar.scss',
})
export class FestivalSidebar implements OnInit {
  private readonly festivalSelection = inject(FestivalSelection);
  private readonly festivalService = inject(FestivalService);
  private readonly destroyRef = inject(DestroyRef);

  private readonly searchSubject = new Subject<string>();

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

    this.searchSubject
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntilDestroyed(this.destroyRef))
      .subscribe((query) => {
        this.festivalService.setSearchQuery(query);
      });
  }

  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchSubject.next(input.value);
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
