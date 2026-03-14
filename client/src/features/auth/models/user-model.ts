export type Role = 'USER' | 'ADMIN';

export interface User {
  id?: string;
  email: string;
  password?: string;
  role?: Role;
}
