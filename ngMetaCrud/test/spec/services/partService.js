'use strict';

describe('Service: partService', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var partsService;
  beforeEach(inject(function (_partService_) {
    partsService = _partService_;
  }));

  it('should do something', function () {
    expect(!!partService).toBe(true);
  });

});
