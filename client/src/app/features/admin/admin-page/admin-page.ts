import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FestivalService } from '../../festivals/services/festival-service';
import { Festival, FestivalRequest } from '../../festivals/models/festival-model';
import { Observable } from 'rxjs';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { FestivalForm } from '../components/festival-form/festival-form';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [CommonModule, ButtonModule, TableModule, DialogModule, FestivalForm, ToastModule],
  providers: [MessageService],
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.scss',
})
export class AdminPage {
  private readonly festivalService = inject(FestivalService);
  private readonly messageService = inject(MessageService);

  readonly festivals$: Observable<Festival[]> = this.festivalService.getAll$();

  displayDialog = false;
  selectedFestival: Festival | null = null;
  isReadOnly = false;

  showDialog(festival: Festival | null = null, readOnly = false): void {
    this.selectedFestival = festival;
    this.isReadOnly = readOnly;
    this.displayDialog = true;
  }

  hideDialog(): void {
    this.displayDialog = false;
    this.selectedFestival = null;
    this.isReadOnly = false;
  }

  saveFestival(festivalRequest: FestivalRequest): void {
    const obs = this.selectedFestival
      ? this.festivalService.update$(this.selectedFestival.id, festivalRequest)
      : this.festivalService.create$(festivalRequest);

    obs.subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Succès',
          detail: `Le festival a été ${this.selectedFestival ? 'mis à jour' : 'créé'} avec succès.`,
        });
        this.hideDialog();
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: "Une erreur est survenue lors de l'enregistrement.",
        });
      },
    });
  }

  deleteFestival(event: Event, id: number): void {
    event.stopPropagation(); // Empêcher l'ouverture de la modal lors du clic sur supprimer
    if (confirm('Êtes-vous sûr de vouloir supprimer ce festival ?')) {
      this.festivalService.delete$(id).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Supprimé',
            detail: 'Le festival a été supprimé.',
          });
        },
      });
    }
  }
}
