'use strict';

describe('Controller: FacetCtrl', function () {

  // load the controller's module
  beforeEach(module('ngSearchFacetsApp'));

  var FacetCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
      FacetCtrl = $controller('FacetCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });

  it('should list facets with one or more values as categories', function () {
    expect(scope.facets)
  });
});
