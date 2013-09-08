'use strict';

describe('Controller: BillofmaterialsCtrl', function () {

  // load the controller's module
  beforeEach(module('ngSearchFacetsApp'));

  var BillofmaterialsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BillofmaterialsCtrl = $controller('BillofmaterialsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
