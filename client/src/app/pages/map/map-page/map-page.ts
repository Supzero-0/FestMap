import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { FestivalMap } from '../../../features/festivals/components/festival-map/festival-map';
import { FestivalList } from '../../../features/festivals/components/festival-list/festival-list';
import { ButtonModule } from 'primeng/button';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../features/auth/services/auth-service';

@Component({
  selector: 'app-map-page',
  imports: [CommonModule, FestivalMap, FestivalList, ButtonModule, RouterLink],
  templateUrl: './map-page.html',
  styleUrl: './map-page.scss',
})
export class MapPage {
  @ViewChild(FestivalMap) mapCmp!: FestivalMap;

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  onSelectFestival(id: number) {
    this.mapCmp?.focusMarker(id);
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
