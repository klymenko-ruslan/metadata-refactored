'use strict';

describe('Service: searchService', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var searchService;
  beforeEach(inject(function (_searchService_) {
    searchService = _searchService_;
  }));

  it('should do something', function () {
    expect(!!searchService).toBe(true);
  });

});