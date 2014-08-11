'use strict';

describe('Controller: LoginCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var $httpBackend,
      $routeParams,
    LoginCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, _$httpBackend_, _$routeParams_, $rootScope) {
    $httpBackend = _$httpBackend_;
    $routeParams = _$routeParams_;
    scope = $rootScope.$new();
    LoginCtrl = $controller('LoginCtrl', {
      $scope: scope
    });
  }));

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  describe('login()', function() {
    it('should issue a POST request', function() {
      scope.email = 'admin@turbointernational.com';
      scope.password = 'adminpw';

      $httpBackend.expectPOST('/metadata/security/login', 'j_username=admin%40turbointernational.com&j_password=adminpw').respond();
      $httpBackend.expectGET('/metadata/security/user/myroles').respond([]);
      $httpBackend.expectGET('/metadata/security/user/me').respond({id: 1, name: 'Admin', email: scope.email});

      scope.login();

      $httpBackend.flush();
    });
  });

  describe('resetRequest()', function() {

    it('should issue a POST request', function() {
      scope.email = 'admin@turbointernational.com';

      $httpBackend.expectPOST('/metadata/security/password/reset/request', 'email=admin%40turbointernational.com').respond();

      scope.resetRequest();

      $httpBackend.flush();
    });
  });

  describe('resetToken()', function() {
    it('should issue a POST request', function() {
      scope.password = 'foopw';

      $routeParams.token = 'fooToken'

      $httpBackend.expectPOST('/metadata/security/password/reset/token/' + $routeParams.token, 'password=foopw').respond();

      scope.resetToken();

      $httpBackend.flush();
    });
  });

});
