'use strict';

describe('ChlogSrcLinkDlgCtrl:', function() {

  var $scope, $uibModalInstance, cbSave, doc;
  var sourcesNames = {}, lastPicked = {};

  beforeEach(module('ngMetaCrudApp'));

  beforeEach(inject(function($controller, $rootScope) {
    $scope = $rootScope.$new();
    $uibModalInstance = {
      close: jasmine.createSpy('close')
    }
    cbSave = jasmine.createSpy('cbSave');
    var dummyElement = document.createElement('div');
    console.log('dummyElement: ' + document.getElementById);
    jasmine.createSpy(document, 'getElementById').and
      .callFake(function(id) {
        console.log('ID: ' + id);
        return dummyElement;
      });
    $controller('ChlogSrcLinkDlgCtrl', {
      $scope: $scope,
      $uibModalInstance: $uibModalInstance,
      cbSave: cbSave,
      sourcesNames: sourcesNames,
      lastPicked: lastPicked,
      cancelUrl: 'http://localhost:8080/foo/cancel',
      begin: null
    });
  }));

  xit('should be initialized', function() {
  });

});
