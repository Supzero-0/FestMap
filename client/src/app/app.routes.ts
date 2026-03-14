import { Routes } from '@angular/router';
import { adminGuard } from '../features/admin/guards/admin-guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('../features/festivals/components/festival-map/festival-map').then(
        (m) => m.FestivalMap,
      ),
  },
  {
    path: 'admin',
    loadComponent: () => import('../features/admin/admin-page/admin-page').then((m) => m.AdminPage),
    canActivate: [adminGuard],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
