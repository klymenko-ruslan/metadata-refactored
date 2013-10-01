'use strict';

describe('Controller: TurbomodelCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var TurbomodelCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TurbomodelCtrl = $controller('TurbomodelCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
