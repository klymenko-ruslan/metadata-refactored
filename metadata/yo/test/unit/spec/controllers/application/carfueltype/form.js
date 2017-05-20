'use strict';

describe('CarFuelTypeFormCtrl: ', function() {

  beforeEach(module('ngMetaCrudApp'));

  var $controller, $scope, $q, $location, toastr, restService;

  beforeEach(inject(function(_$controller_, _$q_, $rootScope, _$location_,
    _toastr_, _restService_)
  {
    $controller = _$controller_;
    $q = _$q_;
    $scope = $rootScope.$new();
    $location = _$location_;
    toastr = _toastr_;
    restService = _restService_;
  }));

  describe('mode -- create:', function() {

    beforeEach(function() {
      $controller('CarFuelTypeFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr
      });
    });

    it('should initialize initial state', function() {
      expect($scope.carfueltype).toEqual({});
    });

    it('should save', function() {
      $scope.carfueltype = {name: 'Alcohol'};
      spyOn(restService, 'createCarfueltype').and
        .returnValue($q(function(resolve) { resolve(4); }));
      spyOn(toastr, 'success');
      $scope.save();
      $scope.$apply();
      expect(restService.createCarfueltype)
        .toHaveBeenCalledWith($scope.carfueltype);
      expect(toastr.success).toHaveBeenCalledWith('Carfueltype "Alcohol" ' +
        'has been successfully created.');
      expect($location.path()).toBe('/application/carfueltype/list');
    });

  });

});
