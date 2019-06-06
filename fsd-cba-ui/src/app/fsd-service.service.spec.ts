import { TestBed } from '@angular/core/testing';

import { FsdServiceService } from './fsd-service.service';

describe('FsdServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FsdServiceService = TestBed.get(FsdServiceService);
    expect(service).toBeTruthy();
  });
});
