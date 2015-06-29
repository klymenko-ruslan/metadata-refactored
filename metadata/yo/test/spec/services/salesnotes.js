'use strict';

describe('Service: Salesnotes', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var Salesnotes;
  beforeEach(inject(function (_Salesnotes_) {
    Salesnotes = _Salesnotes_;
  }));

  it('should do something', function () {
    expect(!!Salesnotes).toBe(true);
  });

});
