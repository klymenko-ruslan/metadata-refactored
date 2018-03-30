'use strict';

describe('CarModelengineyearViewCtrl: ', function() {

  beforeEach(module('ngMetaCrudApp'));

  var $controller, $scope, $q, $routeParams, restService;

  beforeEach(inject(function(_$controller_, _$q_, $rootScope, _restService_) {
    $controller = _$controller_;
    $q = _$q_;
    $scope = $rootScope.$new();
    restService = _restService_;
    $routeParams = {};
  }));

  fit('should initialize initial state', function() {
    $routeParams.id = 14;
    $controller('CarmodelengineyearViewCtrl', {
      $scope: $scope,
      $routeParams: $routeParams,
      restService: restService
    });
    var carmodelengineyear = {};
    spyOn(restService, 'findCarmodelengineyear').and
      .returnValue(123  /*$q(function(resolve) { resolve(carmodelengineyear); })*/);
    $scope.$apply();
    expect($scope.cmeyId).toBe(14);
    expect($scope.carmodelengineyear).toBe(carmodelengineyear);
  });

});
