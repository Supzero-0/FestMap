import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, JsonPipe],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  title = 'FestMap';
  result: any = null;
  loading = false;

  async testHealth() {
    this.loading = true;
    this.result = null;
    try {
      const response = await fetch('/api/health');
      this.result = await response.json();
    } catch (error: any) {
      this.result = { statut: 'error', error: 'Error de connexion au serveur :' + error.message };
    } finally {
      this.loading = false;
    }
  }
}
