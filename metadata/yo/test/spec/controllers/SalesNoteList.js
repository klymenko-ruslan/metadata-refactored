'use strict';

describe('Controller: SalesNoteListCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var ctrl, $scope, $log, $routeParams, NgTableParams, restService, SalesNotes;

  beforeEach(inject(function ($rootScope, $controller,
    _$log_, _$routeParams_, _NgTableParams_, _restService_, _SalesNotes_)
  {
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

});
