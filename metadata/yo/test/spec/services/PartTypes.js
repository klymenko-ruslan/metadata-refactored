'use strict';

describe('Service: PartTypes', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var PartTypes;
  var $httpBackend
  var partTypes
  beforeEach(inject(function ($injector) {
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

  describe('PartTypes.refresh', function() {
    var refreshPromise;

    it('should issue a request to the server and return a promise', function() {

      // Prepare for the call
      $httpBackend.when('GET', '/type/part').respond(function (method, url, data) {
        return [200, partTypes, {}]
      });

      // Issue the call to the service
      refreshPromise = PartTypes.refresh();
      expect(refreshPromise).not.toBeNull();

      describe('PartTypes.refresh details', function() {

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

    it('should return the ')
  });

});
