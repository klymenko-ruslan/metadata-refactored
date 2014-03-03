'use strict';

describe('Service: Facets', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var Facets;
  beforeEach(inject(function (_Facets_) {
    Facets = _Facets_;
  }));

  it('should do something', function () {
    expect(!!Facets).toBe(true);
  });

});
