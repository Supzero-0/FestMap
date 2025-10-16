import { EnvironmentProviders, makeEnvironmentProviders } from '@angular/core';
import { FESTIVAL_API } from '../types';
import { USE_MOCK } from './config';
import { MockFestivalService } from './mock-festival-service';
import { ApiFestivalService } from './api-festival-service';

export function provideFestival(): EnvironmentProviders {
  return makeEnvironmentProviders([
    {
      provide: FESTIVAL_API,
      useExisting: USE_MOCK ? MockFestivalService : ApiFestivalService,
    },
  ]);
}
