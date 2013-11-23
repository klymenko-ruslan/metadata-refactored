'use strict';

describe('Controller: PartcreatemodalCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var PartcreatemodalCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PartcreatemodalCtrl = $controller('PartcreatemodalCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
