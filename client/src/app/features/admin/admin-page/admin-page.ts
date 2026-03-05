import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FestivalService } from '../../festivals/services/festival-service';
import { Festival } from '../../festivals/models/festival-model';
import { Observable } from 'rxjs';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';

@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [CommonModule, ButtonModule, TableModule],
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.scss',
})
export class AdminPage {
  private readonly festivalService = inject(FestivalService);

  readonly festivals$: Observable<Festival[]> = this.festivalService.getAll$();

  deleteFestival(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce festival ?')) {
      this.festivalService.delete$(id).subscribe();
    }
  }
}
