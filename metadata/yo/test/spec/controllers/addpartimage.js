'use strict';

describe('Controller: AddpartimageCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var AddpartimageCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddpartimageCtrl = $controller('AddpartimageCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
