import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { FestivalMap } from '../../../features/festivals/components/festival-map/festival-map';
import { FestivalList } from '../../../features/festivals/components/festival-list/festival-list';
import { ButtonModule } from 'primeng/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-map-page',
  imports: [CommonModule, FestivalMap, FestivalList, ButtonModule, RouterLink],
  templateUrl: './map-page.html',
  styleUrl: './map-page.scss',
})
export class MapPage {
  @ViewChild(FestivalMap) mapCmp!: FestivalMap;

  onSelectFestival(id: number) {
    this.mapCmp?.focusMarker(id);
  }
}
