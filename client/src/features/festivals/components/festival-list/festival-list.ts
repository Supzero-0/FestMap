import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import festivalsData from '../../data/mockFestivals.json';

@Component({
  selector: 'app-festival-list',
  imports: [CommonModule],
  templateUrl: './festival-list.html',
  styleUrl: './festival-list.scss',
})
export class FestivalList {
  festivals = festivalsData;
}
