'use strict';

describe('Controller: LoginCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var $httpBackend,
    $routeParams,
    $rootScope,
    LoginCtrl,
    $location,
    scope,
    toastr;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, _$httpBackend_, _$routeParams_,
    _$location_, _$rootScope_, _toastr_) {

    $httpBackend = _$httpBackend_;
    $routeParams = _$routeParams_;
    $rootScope = _$rootScope_;
    $location = _$location_;
    scope = $rootScope.$new();
    toastr = _toastr_;
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
      scope.username = 'admin@turbointernational.com';
      scope.password = 'adminpw';
      $httpBackend.expectPOST('/metadata/security/login', 'username=admin%40turbointernational.com&password=adminpw').respond();
      scope.login();
      $httpBackend.flush();
      expect($location.path()).toBe('/part/list');
    });
  });

  describe('resetPassword()', function() {
    it('should issue a POST request', function() {
      var $scope, $controller, $uibModalInstance, toastr,
        restService, PasswordResetConfirmDlgCtrl;
      inject(function(_$controller_, _restService_) {
        $controller = _$controller_;
        restService = _restService_;
      });
      $scope = $rootScope.$new();
      $uibModalInstance = jasmine.createSpyObj('$uibModalInstance', ['close']);
      toastr = jasmine.createSpyObj('toastr', ['success', 'error']);
      PasswordResetConfirmDlgCtrl =
        $controller('PasswordResetConfirmDlgCtrl', {
          $scope: $scope,
          $uibModalInstance: $uibModalInstance,
          toastr: toastr,
          restService: restService,
          username: 'admin@turbointernational.com'
        });
      $httpBackend.expectPOST('/metadata/security/password/reset/request',
        'username=admin%40turbointernational.com').respond();
      $scope.onConfirmPasswordResetConfirmDlg();
      $httpBackend.flush();
    });
  });

  describe('resetToken()', function() {
    it('should issue a POST request', function() {
      scope.password = 'foopw';
      $routeParams.token = 'fooToken';
      $httpBackend.expectPOST('/metadata/security/password/reset/token/' +
          $routeParams.token, 'password=foopw').respond();
      scope.resetToken();
      $httpBackend.flush();
    });
  });

});
