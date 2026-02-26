import { AfterViewInit, Component, DestroyRef, inject, OnDestroy } from '@angular/core';
import type {
  Map as LeafletMap,
  Marker as LeafletMarker,
  FeatureGroup as LeafletFeatureGroup,
} from 'leaflet';

import { Festival } from '../../types';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MessageService } from 'primeng/api';
import { FestivalService } from '../../services/festival-service';

@Component({
  selector: 'app-festival-map',
  imports: [],
  templateUrl: './festival-map.html',
  styleUrl: './festival-map.scss',
})
export class FestivalMap implements AfterViewInit, OnDestroy {
  private readonly festivalService = inject(FestivalService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly messageService = inject(MessageService);

  private L!: typeof import('leaflet');

  private map!: LeafletMap;
  private markersGroup!: LeafletFeatureGroup;
  private markersById = new Map<number, LeafletMarker>();

  private static leafletPromise?: Promise<typeof import('leaflet')>;

  private async loadLeaflet(): Promise<typeof import('leaflet')> {
    if (!FestivalMap.leafletPromise) {
      FestivalMap.leafletPromise = import('leaflet');
    }
    return FestivalMap.leafletPromise;
  }

  ngAfterViewInit(): void {
    void this.bootstrap();
  }

  private async bootstrap(): Promise<void> {
    try {
      this.L = await this.loadLeaflet();

      this.setupDefaultMarkerIcon();
      this.initMap();

      this.festivalService
        .getAll$()
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe({
          next: (festivals) => this.renderMarkers(festivals),
          error: (err) => {
            console.error('[FestivalMap] getAll$ error:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Impossible de charger les festivals.',
            });
          },
        });

      setTimeout(() => this.map.invalidateSize(), 0);
    } catch (e) {
      console.error('[FestivalMap] Leaflet load/init error:', e);
      this.messageService.add({
        severity: 'error',
        summary: 'Erreur',
        detail: "Impossible d'initialiser la carte.",
      });
    }
  }

  private setupDefaultMarkerIcon(): void {
    const DefaultIcon = this.L.icon({
      iconRetinaUrl: 'leaflet/marker-icon-2x.png',
      iconUrl: 'leaflet/marker-icon.png',
      shadowUrl: 'leaflet/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      tooltipAnchor: [16, -28],
      shadowSize: [41, 41],
    });

    (this.L.Marker as any).prototype.options.icon = DefaultIcon;
  }

  private initMap(): void {
    this.map = this.L.map('festivalMap', {
      center: [46.8, 2.5],
      zoom: 6,
    });

    this.L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
    }).addTo(this.map);

    this.markersGroup = this.L.featureGroup();
    this.markersGroup.addTo(this.map);
  }

  private renderMarkers(festivals: Festival[]): void {
    if (!this.markersGroup) return;

    this.markersGroup.clearLayers();
    this.markersById.clear();

    festivals?.forEach((f) => {
      if (f.latitude == null || f.longitude == null) return;

      const marker = this.L.marker([f.latitude, f.longitude], {
        title: f.name,
        alt: f.name,
      }).bindPopup(`
          <section data-testid="festival-popup-${f.id}">
            <strong>${f.name}</strong>
            <br>${f.city}
          </section>
        `);

      marker.on('add', () => {
        const elem = marker.getElement();
        if (elem) elem.setAttribute('data-testid', `festival-marker-${f.id}`);
      });

      this.markersGroup.addLayer(marker);
      this.markersById.set(f.id, marker as unknown as LeafletMarker);
    });

    const count = (this.markersGroup.getLayers() || []).length;
    if (count > 0) {
      this.map.fitBounds(this.markersGroup.getBounds().pad(0.2));
    }
  }

  focusMarker(id: number): void {
    const marker = this.markersById.get(id);
    if (!marker || !this.map) return;

    const ll = marker.getLatLng();
    this.map.stop();
    const targetZoom = Math.max(this.map.getZoom(), 9);
    this.map.flyTo(ll, targetZoom, { animate: true, duration: 0.4, easeLinearity: 0.25 });
    marker.openPopup();
  }

  ngOnDestroy(): void {
    try {
      if (this.map) {
        this.markersGroup?.clearLayers();
        this.map.off();
        this.map.remove();
      }
    } catch {
      // safe-guard
    }
  }
}
