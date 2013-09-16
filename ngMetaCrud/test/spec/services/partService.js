'use strict';

describe('Service: restService', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var partsService;
  beforeEach(inject(function (_restService_) {
    partsService = _restService_;
  }));

  it('should do something', function () {
    expect(!!restService).toBe(true);
  });

});
