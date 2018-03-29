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
      $scope: scope,
      $location: $location
    });
    $httpBackend.whenGET('views/security/login.html').respond();
    $httpBackend.whenGET('/metadata/security/user/me').respond(
        '{' +
          '"id": 1,' +
          '"name": "foo",' +
          '"email": "foo@bar.com",' +
          '"username": "foo@bar.com",' +
          '"enabled": true,' +
          '"authProvider": null,' +
          '"groups": [' +
          '  {' +
          '    "id": 1,' +
          '    "name": "Reader",' +
          '    "roles": [' +
          '      {' +
          '        "id": 22,' +
          '        "name": "ROLE_CHLOGSRC_READ",' +
          '        "display": "Read a changelog source."' +
          '      }' +
          '    ]' +
          '  }' +
          ']' +
        '}');
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
      spyOn($location, 'path').and.stub();
      scope.login();
      $httpBackend.flush();
      expect($location.path).toHaveBeenCalledWith('/part/list');
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
