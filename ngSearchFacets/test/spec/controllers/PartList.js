'use strict';

describe('Controller: PartlistCtrl', function () {

  // load the controller's module
  beforeEach(module('ngSearchFacetsApp'));

  var PartlistCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PartlistCtrl = $controller('PartlistCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
