'use strict';

describe('CarEngineFormCtrl: ', function() {

  beforeEach(module('ngMetaCrudApp'));

  var $controller, $q, $scope, $location, toastr;

  beforeEach(inject(function(_$controller_, _$q_, $httpBackend, $rootScope,
    _$location_, _toastr_)
  {
    $controller = _$controller_;
    $q = _$q_;
    $scope = $rootScope.$new();
    $location = _$location_;
    toastr = _toastr_;
    $httpBackend.whenGET('views/application/carengine/list.html').respond();
  }));

  describe('mode -- create:', function() {

    var carEngine = null, carFuelTypes = [{id: 3, name: 'D'}];

    beforeEach(function() {
      $controller('CarEngineFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carEngine: carEngine,
        carFuelTypes: carFuelTypes
      });
    });

    it('should initialize initial state', function() {
      expect($scope.carFuelTypes).toBe(carFuelTypes);
      expect($scope.carEngine).toBe(carEngine);
      expect($scope.carengineId).toBeNull(); // mode -- create
    });

    it('should save new car engine instance', function() {
      var newCarEngine = {id:6, engineSize: '37690'};
      $scope.$on('carengineform:save', function(event, callback) {
        callback($q(function(resolve) {
          resolve(newCarEngine);
        }));
      });
      spyOn(toastr, 'success');
      $scope.save(); // fire the event
      $scope.$apply();
      expect(toastr.success).toHaveBeenCalledWith('Car engine [6] - ' +
        '"37690" has been successfully created.');
      expect($location.path()).toBe('/application/carengine/list');
    });

  });

  describe('mode -- edit:', function() {

    var carEngine = {id:6, engineSize: '37690'},
      carFuelTypes = [{id: 3, name: 'D'}];

    beforeEach(function() {
      $controller('CarEngineFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carEngine: carEngine,
        carFuelTypes: carFuelTypes
      });
    });

    it('should initialize initial state', function() {
      expect($scope.carFuelTypes).toBe(carFuelTypes);
      expect($scope.carEngine).toBe(carEngine);
      expect($scope.carengineId).toBe(6); // mode -- edit
    });

    it('should update the car engine instance', function() {
      $scope.$on('carengineform:save', function(event, callback) {
        callback($q(function(resolve) {
          resolve(carEngine);
        }));
      });
      spyOn(toastr, 'success');
      $scope.save(); // fire the event
      $scope.$apply();
      expect(toastr.success).toHaveBeenCalledWith('Car engine [6] - ' +
        '"37690" has been successfully updated.');
      expect($location.path()).toBe('/application/carengine/list');
    });

  });

  describe('event \'form:created\':', function() {

    beforeEach(function() {
      $controller('CarEngineFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carEngine: null,
        carFuelTypes: null
      });
    });

    it('should be processed', function() {
      $scope.$emit('form:created', {
        name: 'carengineForm',
        controller: function() {}
      });
      expect($scope.carengineForm).not.toBeNull();
    });

    it('should NOT be processed', function() {
      $scope.$emit('form:created', {
        name: 'wrongForm',
        controller: function() {}
      });
      expect($scope.carengineForm).toBeUndefined();
    });

 });

});
