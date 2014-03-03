'use strict';

describe('Controller: MyAccountCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var MyAccountCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MyAccountCtrl = $controller('MyAccountCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
