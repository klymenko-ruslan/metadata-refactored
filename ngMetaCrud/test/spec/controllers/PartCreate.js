'use strict';

describe('Controller: PartcreateCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var PartcreateCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PartcreateCtrl = $controller('PartcreateCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
