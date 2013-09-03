'use strict';

describe('Service: facetNameService', function () {

  // load the service's module
  beforeEach(module('ngSearchFacetsApp'));

  // instantiate service
  var facetNameService;
  beforeEach(inject(function (_facetNameService_) {
    facetNameService = _facetNameService_;
  }));

  it('should do something', function () {
    expect(!!facetNameService).toBe(true);
  });

});
