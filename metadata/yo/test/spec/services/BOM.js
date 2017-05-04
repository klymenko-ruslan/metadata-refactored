'use strict';

describe('Service: Bom', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var Bom;
  beforeEach(inject(function (_Bom_) {
dump(_Bom_);
      Bom = _Bom_;
  }));

  it('should do something', function () {
dump(Bom);
    expect(!!Bom).toBe(true);
  });

});
