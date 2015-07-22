'use strict';

describe('Service: Kits', function () {

  // load the service's module
  beforeEach(module('tiMetadataApp'));

  // instantiate service
  var Kits;
  beforeEach(inject(function (_Kits_) {
    Kits = _Kits_;
  }));

  it('should do something', function () {
    expect(!!Kits).toBe(true);
  });

});
