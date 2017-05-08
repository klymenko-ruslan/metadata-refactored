'use strict';

describe('Controller: UsersCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var UserCtrl,
      scope,
      $controller,
      $httpBackend,
      userOne;

  // Initialize the controller and a mock scope
  beforeEach(inject(function (_$controller_, $rootScope, _$httpBackend_) {
    $controller = _$controller_;
    $httpBackend = _$httpBackend_;
console.log('XXX: ');
    scope = $rootScope.$new();

    userOne = {
      "id": 1,
      "name": "Admin"
    };

    $httpBackend.whenGET('/metadata/security/user/me').respond({});
    $httpBackend.whenGET('/metadata/security/user/myroles').respond([]);
    $httpBackend.whenGET('/metadata/security/user').respond([userOne]);

    UserCtrl = $controller('UsersCtrl', {
      $scope: scope
    });

    $httpBackend.flush();
  }));

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('should attach a list of users to the scope', function () {
console.log("scope.users.length: " + scope);
    expect(scope.users.length).toBe(1);
    expect(scope.users[0].id).toEqual(userOne.id);
    expect(scope.users[0].name).toEqual(userOne.name);
  });

});
