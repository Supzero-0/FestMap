export interface Festival {
  id: number;
  name: string;
  description?: string;
  address?: string;
  city: string;
  postalCode?: string;
  country?: string;
  latitude: number;
  longitude: number;
  startDate: string;
  endDate: string;
  genre?: string;
}

export type FestivalRequest = Omit<Festival, 'id'>;
