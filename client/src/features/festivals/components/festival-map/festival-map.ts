import { AfterViewInit, Component, DestroyRef, inject, OnDestroy } from '@angular/core';
import * as L from 'leaflet';
import { Festival, FESTIVAL_API } from '../../types';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

const DefaultIcon = L.icon({
  iconRetinaUrl: 'leaflet/marker-icon-2x.png',
  iconUrl: 'leaflet/marker-icon.png',
  shadowUrl: 'leaflet/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41],
});

(L.Marker as any).prototype.options.icon = DefaultIcon;

@Component({
  selector: 'app-festival-map',
  imports: [],
  templateUrl: './festival-map.html',
  styleUrl: './festival-map.scss',
})
export class FestivalMap implements AfterViewInit, OnDestroy {
  private readonly api = inject(FESTIVAL_API);
  private readonly destroyRef = inject(DestroyRef);

  private map!: L.Map;
  private markersGroup = L.featureGroup();

  ngAfterViewInit(): void {
    this.initMap();

    // Abonnenement aux données (Observables)
    this.api
      .getAll$()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (festivals) => this.renderMarkers(festivals),
        error: (err) => {
          console.error('[FestivalMap] getAll$ error:', err);
        },
      });

    setTimeout(() => this.map.invalidateSize(), 0);
  }

  private initMap(): void {
    this.map = L.map('festivalMap', {
      center: [46.8, 2.5],
      zoom: 6,
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
    }).addTo(this.map);

    this.markersGroup.addTo(this.map);
  }

  private renderMarkers(festivals: Festival[]): void {
    this.markersGroup.clearLayers();

    // Création des marqueurs
    festivals?.forEach((f) => {
      if (f.lat == null || f.lng == null) return;

      const marker = L.marker([f.lat, f.lng], {
        title: f.name,
        alt: f.name,
      }).bindPopup(`<strong>${f.name}</strong><br>${f.city}`);

      // Attribution d'un data-testid
      marker.on('add', () => {
        const elem = marker.getElement();
        if (elem) elem.setAttribute('data-testid', `festival-marker-${f.id}`);
      });

      this.markersGroup.addLayer(marker);
    });

    const count = (this.markersGroup.getLayers() || []).length;
    if (count > 0) {
      this.map.fitBounds(this.markersGroup.getBounds().pad(0.2));
    }
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
