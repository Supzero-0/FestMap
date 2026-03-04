import { AfterViewInit, Component, DestroyRef, inject, OnDestroy, NgZone } from '@angular/core';
import type {
  Map as LeafletMap,
  Marker as LeafletMarker,
  FeatureGroup as LeafletFeatureGroup,
} from 'leaflet';

import { Festival } from '../../types';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MessageService } from 'primeng/api';
import { DateFilter, FestivalService } from '../../services/festival-service';
import { FestivalSelection } from '../../services/festival-selection';
import { CommonModule } from '@angular/common';
import { map, Observable } from 'rxjs';

@Component({
  selector: 'app-festival-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './festival-map.html',
  styleUrl: './festival-map.scss',
})
export class FestivalMap implements AfterViewInit, OnDestroy {
  private readonly festivalService = inject(FestivalService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly messageService = inject(MessageService);
  private readonly festivalSelection = inject(FestivalSelection);
  private readonly ngZone = inject(NgZone);

  private L!: typeof import('leaflet');

  private map!: LeafletMap;
  private markersGroup!: LeafletFeatureGroup;
  private markersById = new Map<number, LeafletMarker>();

  activeDropdown: 'genre' | 'date' | 'country' | null = null;

  readonly filters$ = this.festivalService.getFilters$();

  readonly availableGenres$: Observable<string[]> = this.festivalService
    .getAll$()
    .pipe(
      map((festivals) =>
        [...new Set(festivals.map((f) => f.genre).filter((g): g is string => !!g))].sort(),
      ),
    );

  readonly availableCountries$: Observable<string[]> = this.festivalService
    .getAll$()
    .pipe(map((festivals) => [...new Set(festivals.map((f) => f.address.country))].sort()));

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

  toggleDropdown(dropdown: 'genre' | 'date' | 'country'): void {
    this.activeDropdown = this.activeDropdown === dropdown ? null : dropdown;
  }

  setGenre(genre: string | null): void {
    this.festivalService.setGenre(genre);
    this.activeDropdown = null;
  }

  setCountry(country: string | null): void {
    this.festivalService.setCountry(country);
    this.activeDropdown = null;
  }

  setDateFilter(filter: DateFilter): void {
    this.festivalService.setDateFilter(filter);
    this.activeDropdown = null;
  }

  private async bootstrap(): Promise<void> {
    try {
      this.L = await this.loadLeaflet();

      this.initMap();

      this.festivalService
        .getFiltered$()
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe({
          next: (festivals) => {
            this.renderMarkers(festivals);
          },
          error: (err) => {
            console.error('[FestivalMap] getFiltered$ error:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Impossible de charger les festivals.',
            });
          },
        });

      this.subscribeToSelection();

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

  private subscribeToSelection(): void {
    this.festivalSelection.selectedFestivalId$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((id) => {
        if (id !== null) {
          this.focusMarker(id);
        } else {
          this.map.closePopup();
        }
      });
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
      if (f.address.latitude == null || f.address.longitude == null) return;

      const customIcon = this.L.divIcon({
        className: 'custom-marker-icon',
        html: `<div
            data-testid="festival-marker-${f.id}"
            class="w-7 h-7 rounded-full grid place-items-center
            bg-fest-violet-neon border-2 border-white shadow-lg shadow-black/20">
              <i class="pi pi-map-marker text-white text-sm leading-none translate-x-[0.75px]"></i>
          </div>`,
        iconSize: [24, 24],
        iconAnchor: [12, 12],
        popupAnchor: [0, -12],
        tooltipAnchor: [12, -12],
      });

      const popup = this.L.popup({
        closeButton: false,
        minWidth: 200,
        className: 'festmap-popup',
      });

      const popupContent = (() => {
        const formatDate = (dateString: string) => {
          const options: Intl.DateTimeFormatOptions = {
            year: 'numeric',
            month: 'numeric',
            day: 'numeric',
          };
          return new Date(dateString).toLocaleDateString(undefined, options);
        };

        let descriptionHtml = '';
        if (f.description) {
          descriptionHtml = `<p class="text-fest-text/70 text-sm line-clamp-2 mb-3">${f.description}</p>`;
        }

        let genreHtml = '';
        if (f.genre) {
          genreHtml = `<span class="ml-auto px-2 py-0.5 bg-fest-bg rounded-full">${f.genre}</span>`;
        }

        return `
          <div
            class="bg-fest-surface rounded-lg shadow-md overflow-hidden flex flex-col relative"
            data-testid="festival-card-${f.id}"
          >
            <button id="customCloseButton-${f.id}"
              type="button"
              aria-label="Fermer"
              class="absolute top-3 right-3 z-10 grid place-items-center
                    w-9 h-9 rounded-full
                    bg-fest-bg/70 backdrop-blur
                    ring-1 ring-white/10
                    text-fest-text/80
                    hover:bg-fest-bg hover:text-fest-text
                    transition"
            >
              <i class="pi pi-times text-sm leading-none"></i>
            </button>
            <div class="p-4 flex-1">
              <h3 class="font-semibold text-lg text-fest-text mb-1 pr-8">${f.name}</h3>
              <p class="text-sm text-fest-text/80 mb-2">
                ${f.address.city}, ${f.address.country}
              </p>
              ${descriptionHtml}
              <div class="flex items-center text-xs text-fest-text/60 mb-3">
                <i class="pi pi-calendar mr-1"></i>
                <span>${formatDate(f.startDate)} - ${formatDate(f.endDate)}</span>
                ${genreHtml}
              </div>
            </div>
          </div>
        `;
      })();

      popup.setContent(popupContent);

      const marker = this.L.marker([f.address.latitude, f.address.longitude], {
        title: f.name,
        alt: f.name,
        icon: customIcon,
      }).bindPopup(popup);

      marker.on('click', () => {
        this.ngZone.run(() => {
          this.festivalSelection.selectFestival(f.id);
        });
      });

      popup.on('add', () => {
        const closeButton = popup.getElement()?.querySelector(`#customCloseButton-${f.id}`);
        if (closeButton) {
          closeButton.addEventListener('click', (event) => {
            event.stopPropagation();
            this.map.closePopup();
          });
        }
      });

      popup.on('remove', () => {
        this.ngZone.run(() => {
          if (this.festivalSelection.selectedFestivalId === f.id) {
            this.festivalSelection.selectFestival(null);
          }
        });
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

    // Zoom to marker
    this.map.flyTo(ll, targetZoom, {
      animate: true,
      duration: 0.4,
      easeLinearity: 0.25,
    });

    // Only open popup if not already open
    if (!marker.isPopupOpen()) {
      marker.openPopup();
    }
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
