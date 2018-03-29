'use strict';

describe('Controller: GroupsCtrl', function () {

  beforeEach(module('ngMetaCrudApp'));

  var $controller, $q, $scope, restService;

  // Initialize the controller and a mock scope
  beforeEach(inject(function(_$controller_, _$q_, $rootScope, $httpBackend, _restService_) {
    $controller = _$controller_;
    $q = _$q_;
    $scope = $rootScope.$new();
    restService = _restService_;
    $httpBackend.when('GET', 'views/security/login.html').respond();
  }));

  it('should attach a list of groups to the scope', function () {
    spyOn(restService, 'getGroups').and.returnValue($q(
      function (resolve) {
        resolve([]);
      }
    ));
    $controller('GroupsCtrl', {
      $log: jasmine.createSpyObj('$log', ['log']),
      restService: restService,
      $scope: $scope,
    });
    $scope.$apply();
    expect($scope.groups).toBeDefined();
    expect($scope.groups.length).toBe(0);
  });

});
