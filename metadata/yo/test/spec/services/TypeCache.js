'use strict';

describe('Service: TypeCache', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var TypeCache;
  beforeEach(inject(function (_TypeCache_) {
    TypeCache = _TypeCache_;
  }));

  it('should do something', function () {
    expect(!!TypeCache).toBe(true);
  });

});
