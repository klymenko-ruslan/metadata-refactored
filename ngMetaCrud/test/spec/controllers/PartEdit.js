'use strict';

describe('Controller: ParteditCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var ParteditCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ParteditCtrl = $controller('ParteditCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
