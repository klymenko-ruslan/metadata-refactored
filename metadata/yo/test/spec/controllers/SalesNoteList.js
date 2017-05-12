'use strict';

describe('Controller: SalesNoteListCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var ctrl, $scope, $q;

  beforeEach(inject(function (_$q_, $rootScope, $controller,
    _$log_, _$routeParams_, _NgTableParams_, _restService_, _SalesNotes_)
  {
    $q = _$q_;
    $scope = $rootScope.$new();
    ctrl = $controller('SalesNoteListCtrl', {
      $scope: $scope,
      $log: _$log_,
      $routeParams: _$routeParams_,
      NgTableParams: _NgTableParams_,
      restService: _restService_,
      SalesNotes: _SalesNotes_,
      primaryPartId: null
    });
  }));

  it('should initialize \'states\'', function() {
    expect($scope.states).toBeDefined();
    expect($scope.states).not.toBeNull();
  });

  it('should initialize \'search\'', function() {
    expect($scope.search).toBeDefined();
    expect($scope.search).not.toBeNull();
    expect($scope.search.partNumber).toBeNull();
    expect($scope.search.includePrimary).toBe(true);
    expect($scope.search.includeRelated).toBe(true);
    expect($scope.search.states).toEqual([]);
    expect($scope.search.comment).toBeNull();
  });

  it('should not initialize \'part\'', function() {
    expect($scope.partId).not.toBeNull();
    expect($scope.partPromise).not.toBeDefined();
    expect($scope.part).toBeNull();
  });

  describe('when partId defined', function() {

    var ctrl, $scope, restService;

    beforeEach(inject(function ($rootScope, $controller, _$routeParams_,
      _restService_)
    {
      _$routeParams_.id = 1001;
      $scope = $rootScope.$new();
      restService = _restService_;
      spyOn(restService, 'findPart').and.returnValue($q.resolve(
        {
          id: 1001,
          partType: {
            name: 'Turbo'
          }
        }
      ));
      ctrl = $controller('SalesNoteListCtrl', {
        $scope: $scope,
        restService: restService,
        primaryPartId: null
      });
      $scope.$apply();
    }));

    it('should load (primary) \'part\'', function() {
      expect($scope.partId).toBe(1001);
      expect(restService.findPart).toHaveBeenCalledWith(1001);
      expect($scope.partPromise).not.toBeNull();
      expect($scope.part).not.toBeNull();
      expect($scope.part.id).toBe(1001);
      expect($scope.partType).toBe('Turbo');
    });

  });

});
