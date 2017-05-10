'use strict';

describe('Controller: GroupsCtrl', function () {

  beforeEach(module('ngMetaCrudApp'));

  var $rootScope, $scope, restService;

  // Initialize the controller and a mock scope
  beforeEach(inject(function (_$controller_, _$rootScope_, _$q_) {
    $rootScope = _$rootScope_;
    $scope = $rootScope.$new();
    restService = { getGroups: null };
    spyOn(restService, 'getGroups').and.returnValue(_$q_(
      function (resolve, reject) {
        resolve([]);
      }
    ));
    _$controller_('GroupsCtrl', {
      $log: jasmine.createSpyObj('$log', ['log']),
      restService: restService,
      $scope: $scope,
    });
    $scope.$apply(); // resolve promise(s)
  }));

  it('should attach a list of groups to the scope', function () {
    expect($scope.groups).toBeDefined();
    expect($scope.groups.length).toBe(0);
  });

});
