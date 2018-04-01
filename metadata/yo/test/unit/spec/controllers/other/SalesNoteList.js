'use strict';

describe('Controller: SalesNoteListCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var $scope, $controller, $routeParams, $q, NgTableParams, restService;

  beforeEach(inject(function (_$q_, $rootScope, _$controller_, _restService_,
        _NgTableParams_, $httpBackend)
  {
    $q = _$q_;
    NgTableParams = _NgTableParams_;
    $controller = _$controller_;
    restService = _restService_;
    $scope = $rootScope.$new();
    $routeParams = {};
    $httpBackend.whenGET('views/security/login.html').respond();
  }));

  describe('test initial state', function() {

    beforeEach(function () {
      $controller('SalesNoteListCtrl', {
        $scope: $scope,
        primaryPartId: null,
        $routeParams: $routeParams,
        restService: restService
      });
    });

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

    it('should initialize \'notesTableParams\'', function() {
      expect($scope.notesTableParams).toBeDefined();
      expect($scope.notesTableParams).not.toBeNull();
      var parameters = $scope.notesTableParams.parameters();
      expect(parameters.page).toBe(1);
      expect(parameters.count).toBe(10);
      expect(true).toBe($scope.notesTableParams.isSortBy('createDate', 'desc'));
    });

  });

  describe('testing search filter functionality', function() {

    beforeEach(function() {
      $controller('SalesNoteListCtrl', {
        $scope: $scope,
        primaryPartId: null,
        $routeParams: $routeParams,
        restService: restService,
        SalesNotes: {},
        NgTableParams: NgTableParams
      });
    });

    it('changing \'partNumber\' triggers table reload', function() {
      spyOn($scope.notesTableParams, 'reload').and.callThrough();
      spyOn(restService, 'filterSalesNotes').and.returnValue($q.resolve({
        hits: 0
      }));

      $scope.$apply();
      $scope.search.partNumber = '1-A-0001';
      $scope.$apply();
      expect($scope.notesTableParams.reload.calls.count()).toBe(1);
      expect(restService.filterSalesNotes).toHaveBeenCalledWith('1-A-0001',
        null, null, true, true, [ 'draft', 'submitted', 'approved',
          'published'], 'createDate', 'desc', 0, 10);
      expect(restService.filterSalesNotes.calls.count()).toBe(1);
    });

    it('changing \'includePrimary\' triggers table reload', function() {
      spyOn($scope.notesTableParams, 'reload').and.callThrough();
      spyOn(restService, 'filterSalesNotes').and.returnValue($q.resolve({
        hits: 0
      }));
      $scope.$apply();
      $scope.search.includePrimary = false;
      $scope.$apply();
      expect($scope.notesTableParams.reload.calls.count()).toBe(1);
      expect(restService.filterSalesNotes).toHaveBeenCalledWith(null,
        null, null, false, true, [ 'draft', 'submitted', 'approved',
          'published'], 'createDate', 'desc', 0, 10);
      expect(restService.filterSalesNotes.calls.count()).toBe(1);
    });

    it('changing \'includeRelated\' triggers table reload', function() {
      spyOn($scope.notesTableParams, 'reload').and.callThrough();
      spyOn(restService, 'filterSalesNotes').and.returnValue($q.resolve({
        hits: 0
      }));
      $scope.$apply();
      $scope.search.includeRelated = false;
      $scope.$apply();
      expect($scope.notesTableParams.reload.calls.count()).toBe(1);
      expect(restService.filterSalesNotes).toHaveBeenCalledWith(null,
        null, null, true, false, [ 'draft', 'submitted', 'approved',
          'published'], 'createDate', 'desc', 0, 10);
      expect(restService.filterSalesNotes.calls.count()).toBe(1);
    });

    it('changing \'states\' triggers table reload', function() {
      spyOn($scope.notesTableParams, 'reload').and.callThrough();
      spyOn(restService, 'filterSalesNotes').and.returnValue($q.resolve({
        hits: 0
      }));
      $scope.$apply();
      $scope.search.states = ['draft', 'approved'];
      $scope.$apply();
      expect($scope.notesTableParams.reload.calls.count()).toBe(1);
      expect(restService.filterSalesNotes).toHaveBeenCalledWith(null,
        null, null, true, true, [ 'draft', 'approved', ],
        'createDate', 'desc', 0, 10);
      expect(restService.filterSalesNotes.calls.count()).toBe(1);
    });

    it('changing \'comment\' triggers table reload', function() {
      spyOn($scope.notesTableParams, 'reload').and.callThrough();
      spyOn(restService, 'filterSalesNotes').and.returnValue($q.resolve({
        hits: 0
      }));
      $scope.$apply();
      $scope.search.comment = 'Hello world!';
      $scope.$apply();
      expect($scope.notesTableParams.reload.calls.count()).toBe(1);
      expect(restService.filterSalesNotes).toHaveBeenCalledWith(null,
        'Hello world!', null, true, true, [ 'draft', 'submitted', 'approved',
          'published'], 'createDate', 'desc', 0, 10);
      expect(restService.filterSalesNotes.calls.count()).toBe(1);
    });

  });

  describe('testing states update functionality', function() {

    beforeEach(function() {
      $controller('SalesNoteListCtrl', {
        $scope: $scope,
        primaryPartId: null,
        $routeParams: $routeParams,
        restService: restService,
        SalesNotes: {},
        NgTableParams: NgTableParams
      });
      spyOn($scope.notesTableParams, 'reload').and.stub();
    });

    it('should update \'search.states\' when \'states.current.draft\' changed',
      function() {
        $scope.states.current.draft = true;
        $scope.$apply();
        expect($scope.search.states).toEqual(jasmine.arrayContaining(
          ['draft']));
        $scope.states.current.draft = false;
        $scope.$apply();
        expect($scope.search.states).not.toEqual(
          jasmine.arrayContaining(['draft']));
      }
    );

    it('should update \'search.states\' when \'states.current.submitted\'' +
      ' changed',
      function() {
        $scope.states.current.submitted = true;
        $scope.$apply();
        expect($scope.search.states).toEqual(jasmine.arrayContaining(
          ['submitted']));
        $scope.states.current.submitted = false;
        $scope.$apply();
        expect($scope.search.states).not.toEqual(
          jasmine.arrayContaining(['submitted']));
      }
    );

    it('should update \'search.states\' when \'states.current.approved\'' +
      ' changed',
      function() {
        $scope.states.current.approved = true;
        $scope.$apply();
        expect($scope.search.states).toEqual(jasmine.arrayContaining(
          ['approved']));
        $scope.states.current.approved = false;
        $scope.$apply();
        expect($scope.search.states).not.toEqual(
          jasmine.arrayContaining(['approved']));
      }
    );

    it('should update \'search.states\' when \'states.current.rejected\'' +
      ' changed',
      function() {
        $scope.states.current.rejected = true;
        $scope.$apply();
        expect($scope.search.states).toEqual(jasmine.arrayContaining(
          ['rejected']));
        $scope.states.current.rejected = false;
        $scope.$apply();
        expect($scope.search.states).not.toEqual(
          jasmine.arrayContaining(['rejected']));
      }
    );

    it('should update \'search.states\' when \'states.current.published\'' +
      ' changed',
      function() {
        $scope.states.current.published = true;
        $scope.$apply();
        expect($scope.search.states).toEqual(jasmine.arrayContaining(
          ['published']));
        $scope.states.current.published = false;
        $scope.$apply();
        expect($scope.search.states).not.toEqual(
          jasmine.arrayContaining(['published']));
      }
    );

  });

  describe('when partId defined', function() {

    beforeEach(inject(function ($rootScope, $controller) {
      $routeParams.id = 1001;
      $scope = $rootScope.$new();
      spyOn(restService, 'findPart').and.returnValue($q.resolve(
        {
          id: 1001,
          partType: {
            name: 'Turbo'
          }
        }
      ));
      $controller('SalesNoteListCtrl', {
        $scope: $scope,
        $routeParams: $routeParams,
        restService: restService,
        primaryPartId: null,
        SalesNotes: {},
        NgTableParams: NgTableParams
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
