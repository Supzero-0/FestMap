import { Role } from './user-model';

export interface AuthResponse {
  token: string;
  email: string;
  role: Role;
}
