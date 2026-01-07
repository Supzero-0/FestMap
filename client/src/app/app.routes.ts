import { Routes } from '@angular/router';
import { MapPage } from '../pages/map/map-page/map-page';
import { LoginPageComponent } from '../pages/auth/login/login-page';
import { RegisterPageComponent } from '../pages/auth/register/register-page';

export const routes: Routes = [
  { path: '', component: MapPage },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
];
