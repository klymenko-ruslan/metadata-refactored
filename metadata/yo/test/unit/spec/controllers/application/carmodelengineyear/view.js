'use strict';

describe('CarmodelengineyearViewCtrl: ', function() {

  beforeEach(module('ngMetaCrudApp'));

  var $controller, $scope, $q, $routeParams, restService;

  beforeEach(inject(function(_$controller_, _$q_, $rootScope, _restService_, $httpBackend) {
    $controller = _$controller_;
    $q = _$q_;
    $scope = $rootScope.$new();
    restService = _restService_;
    restService.foo = 'hello';
    $routeParams = {};
    $httpBackend.whenGET('views/security/login.html').respond();
  }));

  it('should initialize initial state', function() {
    $routeParams.id = 14;
    var carmodelengineyear = {};
    spyOn(restService, 'findCarmodelengineyear').and
      .returnValue($q(function(resolve) { resolve(carmodelengineyear); }));
    $controller('CarmodelengineyearViewCtrl', {
      $scope: $scope,
      $routeParams: $routeParams,
      restService: restService
    });
    $scope.$apply();
    expect($scope.cmeyId).toBe(14);
    expect($scope.carmodelengineyear).toBe(carmodelengineyear);
  });

});
