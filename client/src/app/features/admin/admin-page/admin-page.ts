import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FestivalService } from '../../festivals/services/festival-service';
import { UserService } from '../api/user-service';
import { Festival, FestivalRequest } from '../../festivals/models/festival-model';
import { User } from '../../auth/models/user-model';
import { Observable } from 'rxjs';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FestivalForm } from '../components/festival-form/festival-form';
import { UserForm } from '../components/user-form/user-form';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

type AdminView = 'festivals' | 'users';

@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    TableModule,
    DialogModule,
    SelectButtonModule,
    FestivalForm,
    UserForm,
    ToastModule,
  ],
  providers: [MessageService],
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.scss',
})
export class AdminPage {
  private readonly festivalService = inject(FestivalService);
  private readonly userService = inject(UserService);
  private readonly messageService = inject(MessageService);

  readonly festivals$: Observable<Festival[]> = this.festivalService.getAll$();
  readonly users$: Observable<User[]> = this.userService.getAll$();

  currentView: AdminView = 'festivals';
  viewOptions = [
    { label: 'Festivals', value: 'festivals', icon: 'pi pi-map' },
    { label: 'Utilisateurs', value: 'users', icon: 'pi pi-users' },
  ];

  displayFestivalDialog = false;
  displayUserDialog = false;
  selectedFestival: Festival | null = null;
  selectedUser: User | null = null;
  isReadOnly = false;

  showFestivalDialog(festival: Festival | null = null, readOnly = false): void {
    this.selectedFestival = festival;
    this.isReadOnly = readOnly;
    this.displayFestivalDialog = true;
  }

  showUserDialog(user: User | null = null, readOnly = false): void {
    this.selectedUser = user;
    this.isReadOnly = readOnly;
    this.displayUserDialog = true;
  }

  hideDialog(): void {
    this.displayFestivalDialog = false;
    this.displayUserDialog = false;
    this.selectedFestival = null;
    this.selectedUser = null;
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

  saveUser(user: User): void {
    const obs = this.selectedUser?.id
      ? this.userService.update$(String(this.selectedUser.id), user)
      : this.userService.create$(user);

    obs.subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Succès',
          detail: `L'utilisateur a été ${this.selectedUser ? 'mis à jour' : 'créé'} avec succès.`,
        });
        this.hideDialog();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: err.error?.message || "Une erreur est survenue lors de l'enregistrement.",
        });
      },
    });
  }

  deleteFestival(event: Event, id: number): void {
    event.stopPropagation();
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

  deleteUser(event: Event, id: string): void {
    event.stopPropagation();
    if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      this.userService.delete$(id).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Supprimé',
            detail: "L'utilisateur a été supprimé.",
          });
        },
      });
    }
  }
}
