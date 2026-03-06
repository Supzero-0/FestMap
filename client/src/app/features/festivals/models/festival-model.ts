export interface Address {
  id: number;
  addressLine: string;
  city: string;
  postalCode: string;
  country: string;
  latitude: number;
  longitude: number;
}

export interface Festival {
  id: number;
  name: string;
  description?: string;
  address: Address;
  startDate: string;
  endDate: string;
  genre?: string;
  favorite?: boolean;
}

export type FestivalRequest = Omit<Festival, 'id'>;
