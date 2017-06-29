'use strict';

describe('ChlogSrcLinkDlgCtrl:', function() {

  var $scope, $uibModalInstance, cbSave;
  var sourcesNames = {}, lastPicked = {};

  beforeEach(module('ngMetaCrudApp'));

  beforeEach(inject(function($controller, $rootScope) {
    $scope = $rootScope.$new();
    $uibModalInstance = {
      close: jasmine.createSpy('close')
    };
    cbSave = jasmine.createSpy('cbSave');
    var div = document.createElement('div', {
      'id': 'upload-preview-template'
    });
    var body = document.createElement('body', {}, [div]);
    var html = document.createElement('html', {}, [body]);
    console.log('html: ' + html);
    document.body.appendChild(html);
    var uploadPreviewTemplate = document.getElementById('upload-preview-template');
    console.log('uploadPreviewTemplate0: ' + uploadPreviewTemplate);

//    jasmine.createSpy(document, 'getElementById').and
//      .returnValue(dummyElement);
    /*
      .callFake(function(id) {
console.log('ID: ' + id);
        return dummyElement;
      });
    */
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
