'use strict';

describe('Controller: GroupsCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var GroupCtrl,
      scope,
      $controller,
      $httpBackend,
      groupOne;

  // Initialize the controller and a mock scope
  beforeEach(inject(function (_$controller_, $rootScope, _$httpBackend_) {
    $controller = _$controller_;
    $httpBackend = _$httpBackend_;
    scope = $rootScope.$new();

    groupOne = {
      "id": 1,
      "name": "Admin"
    };

    $httpBackend.whenGET('/metadata/security/user/me').respond({});
    $httpBackend.whenGET('/metadata/security/user/myroles').respond([]);
    $httpBackend.whenGET('/metadata/security/group').respond([groupOne]);

    GroupCtrl = $controller('GroupsCtrl', {
      $scope: scope
    });

    $httpBackend.flush();
  }));

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('should attach a list of groups to the scope', function () {
    expect(scope.groups.length).toBe(1);
    expect(scope.groups[0].id).toEqual(groupOne.id);
    expect(scope.groups[0].name).toEqual(groupOne.name);
  });

});
