import { TestBed, inject } from '@angular/core/testing';

import { JqueryUtilService } from './jquery-util.service';

describe('JqueryUtilService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [JqueryUtilService]
    });
  });

  it('should be created', inject([JqueryUtilService], (service: JqueryUtilService) => {
    expect(service).toBeTruthy();
  }));
});
