import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { JsonPipe } from '@angular/common';
import { HealthService } from './api/health-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, JsonPipe],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  title = 'FestMap';

  constructor(public healthService: HealthService) {}

  async testHealth() {
    this.healthService.checkHealth();
  }
}