'use strict';

describe('Service: PartTypes', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var $rootScope;
  var PartTypes;
  var $httpBackend;
  var partTypes;
  var refreshPromise;
  beforeEach(inject(function ($injector) {
    $rootScope = $injector.get('$rootScope');
    PartTypes = $injector.get('PartTypes');
    $httpBackend = $injector.get('$httpBackend');
    partTypes = [
      {
        id: 2,
        name: 'cartridge',
        typeName: 'Cartridge'
      },
      {
        id: 3,
        name: 'kit',
        typeName: 'Kit'
      },
      {
        id: 8,
        name: 'fast wearing component',
        typeName: 'Part'
      }
    ];
    refreshPromise = null;
  }));

  it('should not have a value in PartTypes.refreshPromise', function() {
    expect(PartTypes.refreshPromise).toBeNull();
  });

  describe('PartTypes.refresh', function() {

    beforeEach(function(){
      // Prepare for the call
      $httpBackend.whenGET('/metadata/type/part').respond(partTypes);

      // Issue the call to the service
      $rootScope.$apply(function() {
        refreshPromise = PartTypes.refresh();
      });
    });

    it('should put the returned promise in PartTypes.refreshPromise', function() {
      expect(PartTypes.refreshPromise).toBe(refreshPromise);
    });

    it('should not set PartTypes.refreshPromise to null when resolved', function() {
      $httpBackend.flush();
      expect(PartTypes.refreshPromise).toBe(refreshPromise);
    });

    it('should save the response in PartTypes.list', function() {
      $httpBackend.flush();
      expect(PartTypes.list.length).toBe(3);
    });

  });

  describe('PartTypes.getById', function() {

    it('should return the part type with the same id', function() {
      PartTypes.list = partTypes;
      expect(PartTypes.getById(2)).toBe(partTypes[0]);
    });

    it('should return null if no such part type exists', function() {
      PartTypes.list = partTypes;
      expect(PartTypes.getById(0)).not.toBeDefined();
    });
  });
  
  describe('PartTypes.getPromise', function() {
    beforeEach(function() {  
      // Prepare for the call
      $httpBackend.whenGET('/metadata/type/part').respond(partTypes);
    });
    
    it('should return the promise if there is one', function() {

      // Issue the call to the service
      $rootScope.$apply(function() {
        refreshPromise = PartTypes.refresh();
      });
      
      expect(PartTypes.refreshPromise).not.toBeNull();
      expect(PartTypes.getPromise()).toBe(refreshPromise);
    });
  
    it('should refresh the list if there is no promise', function() {
        expect(PartTypes.refreshPromise).toBeNull();
        expect(PartTypes.getPromise()).not.toBeNull();
    });
  });

});
