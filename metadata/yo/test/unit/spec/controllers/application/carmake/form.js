'use strict';

describe('CarMakeFormCtrl: ', function() {

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
    $controller('CarMakeFormCtrl', {
      $scope: $scope,
      $location: $location,
      toastr: toastr
    });
  }));

  it('should react on save a carMake', function () {
    var carMake = { id: 7 };
    $scope.$on('carmakeform:save', function (event, callback) {
      callback($q(function(resolve) {
        resolve(carMake);
      }));
    });
    spyOn(toastr, 'success');
    $scope.save();
    $scope.$apply();
    expect(toastr.success).toHaveBeenCalledWith('Carmake [7] has been ' +
      'successfully created.');
    expect($location.path()).toBe('/application/carmake/list');
  });

  describe('event \'form:created\'', function() {

    it('should be handled for carmakeForm', function() {
      $scope.$emit('form:created', {
        name: 'carmakeForm',
        controller: function() {}
      });
      expect($scope.carmakeForm).toBeDefined();
    });

    it('should NOT be handled for not carmakeForm',
      function() {
      $scope.$emit('form:created', {
        name: 'wrongForm',
        controller: function() {}
      });
      expect($scope.carmakeForm).toBeUndefined();
    });

  });

});
