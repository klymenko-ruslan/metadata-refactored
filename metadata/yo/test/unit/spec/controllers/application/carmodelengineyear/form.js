'use strict';

describe('CarModelEngineYearFormCtrl: ', function() {

  beforeEach(module('ngMetaCrudApp'));

  var $controller, $q, $scope, $location, toastr;

  var carEngines = [
    {id: 506, engineSize: "0.6/3", fuelType:{id:3, name:"D"}}
  ];

  var carMakes = [
    {id: 1, name: "Agrale"},
    {id: 2, name: "Aifo"},
    {id: 3, name: "Alaska Diesel"}
  ];

  var carModelEngineYear = {
    engine: {
      engineSize: '2.0/4',
      fuelType: {
        id: 3,
        name: 'D',
        searchId: '3',
        searchSerializer: {}
      },
      id: 214,
      searchId: '214',
      searchSerializer: {}
    },
    id: 877,
    model: {
      id: 1502,
      make: {
        id: 128,
        name: 'Kia',
        searchId: '128',
        searchSerializer: {}
      },
      name: 'MAGENTIS CRDi',
      searchId: '1502',
      searchSerializer: {}
    },
    searchId: '877',
    searchSerializer: {},
    year:{
      id:60,
      name: '2009',
      searchId: '60',
      searchSerializer: {}
    }
  };

  beforeEach(inject(function(_$controller_, _$q_, $rootScope, _$location_,
    $httpBackend, _toastr_)
  {
    $controller = _$controller_;
    $q = _$q_;
    $scope = $rootScope.$new();
    $location = _$location_;
    toastr = _toastr_;
    $httpBackend.whenGET('views/application/carmodelengineyear/list.html').respond();
  }));

  describe('mode -- create:', function() {

    beforeEach(function() {
      $controller('CarModelEngineYearFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carEngines: carEngines,
        carMakes: carMakes,
        carModelEngineYear: null
      });
    });

    it('should initialize initial state', function() {
      expect($scope.carEngines).toBe(carEngines);
      expect($scope.carMakes).toBe(carMakes);
      expect($scope.carModelEngineYear).toBeNull();
      expect($scope.cmeyId).toBeNull(); // mode -- create
    });

    it('should save new application instance', function() {
      $scope.$on('cmeyform:save', function(event, callback) {
        callback($q(function(resolve) {
          resolve(carModelEngineYear);
        }));
      });
      spyOn(toastr, 'success');
      $scope.save(); // fire the event
      $scope.$apply();
      expect(toastr.success).toHaveBeenCalledWith('A new Model Engine Year ' +
        'has been successfully created.');
      expect($location.path()).toBe('/application/carmodelengineyear/list');
    });

  });

  describe('mode -- edit:', function() {

    beforeEach(function() {
      $controller('CarModelEngineYearFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carEngines: carEngines,
        carMakes: carMakes,
        carModelEngineYear: carModelEngineYear
      });
    });

    it('should initialize initial state', function() {
      expect($scope.carEngines).toBe(carEngines);
      expect($scope.carMakes).toBe(carMakes);
      expect($scope.carModelEngineYear).toBe(carModelEngineYear);
      expect($scope.cmeyId).toBe(877); // mode -- edit
    });

    it('should update the carmodelengineyear instance', function() {
      $scope.$on('cmeyform:save', function(event, callback) {
        callback($q(function(resolve) {
          resolve();
        }));
      });
      spyOn(toastr, 'success');
      $scope.save(); // fire the event
      $scope.$apply();
      expect(toastr.success).toHaveBeenCalledWith('The Model Engine Year ' +
        'has been successfully updated.');
      expect($location.path()).toBe('/application/carmodelengineyear/list');
    });

    it('should change location in onClickViewCmey', function() {
      $scope.onClickViewCmey();
      expect($location.path()).toBe('/application/carmodelengineyear/' +
        $scope.cmeyId);
    });

  });

  describe('event \'form:created\':', function() {

    beforeEach(function() {
      $controller('CarModelEngineYearFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carEngines: null,
        carMakes: null,
        carModelEngineYear: null
      });
    });

    it('should be processed', function() {
      $scope.$emit('form:created', {
        name: 'cmeyForm',
        controller: function() {}
      });
      expect($scope.cmeyForm).not.toBeNull();
    });

    it('should NOT be processed', function() {
      $scope.$emit('form:created', {
        name: 'wrongForm',
        controller: function() {}
      });
      expect($scope.cmeyForm).toBeUndefined();
    });

  });

  describe('event cmeyform:revert', function() {

    beforeEach(function() {
      $controller('CarModelEngineYearFormCtrl', {
        $scope: $scope,
        $location: $location,
        toastr: toastr,
        carEngines: null,
        carMakes: null,
        carModelEngineYear: null
      });
    });

    it('should revert', function() {
      spyOn($scope, '$broadcast');
      $scope.revert();
      expect($scope.$broadcast).toHaveBeenCalledWith('cmeyform:revert');
    });

 });

});
