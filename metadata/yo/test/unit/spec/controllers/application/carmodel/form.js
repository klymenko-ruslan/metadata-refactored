'use strict';

describe('CarModelFormCtrl: ', function() {

  beforeEach(module('ngMetaCrudApp'));

  var $controller, $q, $scope, $location, toastr;

  beforeEach(inject(function(_$controller_, _$q_, $rootScope, _$location_,
    _toastr_)
  {
    $controller = _$controller_;
    $q = _$q_;
    $scope = $rootScope.$new();
    $location = _$location_;
    toastr = _toastr_;
  }));

  describe('mode -- create:', function() {

    var carMakes = [], carModel = null;

    beforeEach(function() {
      $controller('CarModelFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carMakes: carMakes,
        carModel: carModel
      });
    });

    it('should initialize initial state', function() {
      expect($scope.carMakes).toBe(carMakes);
      expect($scope.carModel).toBe(carModel);
      expect($scope.carmodelId).toBeNull(); // mode -- create
    });

    it('should save new car engine instance', function() {
      var newCarModel = {id: 1, name: '145', make:{id: 252, name: 'Volvo'}};
      $scope.$on('carmodelform:save', function(event, callback) {
        callback($q(function(resolve) {
          resolve(newCarModel);
        }));
      });
      spyOn(toastr, 'success');
      $scope.save(); // fire the event
      $scope.$apply();
      expect(toastr.success).toHaveBeenCalledWith('Car model [1] - ' +
        '"145" has been successfully created.');
      expect($location.path()).toBe('/application/carmodel/list');
    });

  });

  describe('mode -- edit:', function() {

    var carMakes = [{id: 1, name: 'Agrale'}, {id: 2, name: 'Aifo'}],
      carModel = {id: 1, name: '145', make:{id: 252, name: 'Volvo'}};

    beforeEach(function() {
      $controller('CarModelFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carMakes: carMakes,
        carModel: carModel
      });
    });

    it('should initialize initial state', function() {
      expect($scope.carMakes).toBe(carMakes);
      expect($scope.carModel).toBe(carModel);
      expect($scope.carmodelId).toBe(1); // mode -- edit
    });

    it('should update the car engine instance', function() {
      $scope.$on('carmodelform:save', function(event, callback) {
        callback($q(function(resolve) {
          resolve(carModel);
        }));
      });
      spyOn(toastr, 'success');
      $scope.save(); // fire the event
      $scope.$apply();
      expect(toastr.success).toHaveBeenCalledWith('Car model [1] ' +
        '"145" has been successfully updated.');
      expect($location.path()).toBe('/application/carmodel/list');
    });

  });

  describe('event \'form:created\':', function() {

    beforeEach(function() {
      $controller('CarModelFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carModel: null,
        carMakes: null
      });
    });

    it('should be processed', function() {
      $scope.$emit('form:created', {
        name: 'carmodelForm',
        controller: function() {}
      });
      expect($scope.carengineForm).not.toBeNull();
    });

    it('should NOT be processed', function() {
      $scope.$emit('form:created', {
        name: 'wrongForm',
        controller: function() {}
      });
      expect($scope.carmodelForm).toBeUndefined();
    });

 });

});
