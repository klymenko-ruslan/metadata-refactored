'use strict';

describe('Controller: SearchFacetsCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var SearchFacetsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SearchFacetsCtrl = $controller('SearchFacetsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
