'use strict';

describe('Service: PartTypes', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var $rootScope;
  var PartTypes;
  var $httpBackend;
  var partTypes;
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
  }));

  it('should not have a value in PartTypes.refreshPromise', function() {
    expect(PartTypes.refreshPromise).toBeNull();
  });

  describe('PartTypes.refresh', function() {
    var refreshPromise;

    it('should issue a request to the server and return a promise', function() {

      describe('PartTypes.refresh success', function() {
        beforeEach(function(){
          // Prepare for the call
          $httpBackend.when('GET', '/type/part').respond(function (method, url, data) {
            return [200, partTypes, {}]
          });

          // Issue the call to the service
          $rootScope.$apply(function() {
            refreshPromise = PartTypes.refresh();
          });
        });

        it('should put the returned promise in PartTypes.refreshPromise', function() {
          expect(PartTypes.refreshPromise).toBe(refreshPromise);
        });

        it('should set PartTypes.refreshPromise to null when resolved', function() {
          $httpBackend.flush();
          expect(PartTypes.refreshPromise).toBeNull();
        });

        it('should save the response in PartTypes.list', function() {
          $httpBackend.flush();
          expect(PartTypes.list).toBe(partTypes);
        });

      });

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

});
