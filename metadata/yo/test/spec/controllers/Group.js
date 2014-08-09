'use strict';

describe('Controller: GroupCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var GroupCtrl,
      scope,
      $controller,
      $httpBackend,
      $routeParams,
      users,
      roles;

  // Initialize the controller and a mock scope
  beforeEach(inject(function (_$controller_, _$routeParams_, $rootScope, _$httpBackend_) {
    $controller = _$controller_;
    $httpBackend = _$httpBackend_;
    $routeParams = _$routeParams_;
    scope = $rootScope.$new();

    users = [{
      "id":1,
      "name":"Administrator",
      "email":"admin@example.com",
      "enabled":true,
      "groups":[
        {"id":1,"name":"Admin"}
      ]
    }];

    roles = [
      {"display":"Search and view part information.","id":1},
      {"display":"Add and remove part images.","id":2},
      {"display":"Create parts.","id":3},
      {"display":"Alter existing parts.","id":4},
      {"display":"Delete existing parts.","id":5},
      {"display":"Alter interchangeability.","id":6},
      {"display":"Alter BOM.","id":7},
      {"display":"Alter BOM alternates.","id":8},
      {"display":"Superpowers.","id":9}
    ];

    $httpBackend.whenGET('/metadata/security/user/me').respond(users[0]);
    $httpBackend.whenGET('/metadata/security/user/myroles').respond([]);
    $httpBackend.whenGET('/metadata/security/group/roles').respond(roles);
    $httpBackend.whenGET('/metadata/security/user').respond(users);
  }));

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  describe('Create Mode', function() {
    beforeEach(function() {
      $routeParams.id = 'create';

      GroupCtrl = $controller('GroupCtrl', {
        $scope: scope
      });

      $httpBackend.flush();
    });

    describe('initialization', function() {
      it('should attach a list of users to the scope', function() {
        expect(scope.users['1']).toBeDefined();
        expect(scope.users['1'].id).toEqual(users[0].id);
        expect(scope.users['1'].name).toEqual(users[0].name);
        expect(scope.users['1'].email).toEqual(users[0].email);
        expect(scope.users['1'].enabled).toEqual(users[0].enabled);
        expect(scope.users['1'].groups).toEqual(users[0].groups);
      });

      it('should attach a list of roles to the scope', function() {
        expect(scope.roles).toEqual(roles);

      });

      it('should attach a new group to the scope', function() {
        expect(scope.group.name).toEqual("New Group");
        expect(scope.group.roles).toEqual([]);
        expect(scope.group.users).toEqual([]);
      });
    });

    describe('isNewGroup()', function() {
      it('should return true if the "id" route parameter is create', function() {
        $routeParams.id = 'create';
      });
    });
  });

  describe('Edit Mode', function() {
    beforeEach(function() {
      $routeParams.id = '1';

      $httpBackend.whenGET('/metadata/security/group/' + $routeParams.id).respond({

      });

      GroupCtrl = $controller('GroupCtrl', {
        $scope: scope
      });

      $httpBackend.flush();
    });

    describe('isNewGroup()', function() {
      it('should return true if the "id" route parameter is create', function() {
        $routeParams.id = 'create';
      });
    });
  });

  describe('undo()', function() {
    beforeEach(function() {
      $httpBackend.flush();
    });

    it('should revert changes to the form', function() {

    });
  });
});
