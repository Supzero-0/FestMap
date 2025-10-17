import { AfterViewInit, Component, Input, OnDestroy } from '@angular/core';
import * as L from 'leaflet';
import { Festival } from '../../types';

@Component({
  selector: 'app-festival-map',
  imports: [],
  templateUrl: './festival-map.html',
  styleUrl: './festival-map.scss',
})
export class FestivalMap implements AfterViewInit, OnDestroy {
  @Input() festivals: Festival[] = [];

  private map!: L.Map;

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    this.map = L.map('festivalMap', {
      center: [46.8, 2.5],
      zoom: 6,
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
    }).addTo(this.map);
  }

  ngOnDestroy(): void {
    try {
      if (this.map) {
        this.map.off();
        this.map.remove();
      }
    } catch {
      // safe-guard: ne rien faire si déjà nettoyé
    }
  }
}
