'use strict';

/*global
  _
*/

describe('Controller: GroupCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var GroupCtrl,
      scope,
      $controller,
      dialogs,
      $httpBackend,
      $location,
      $routeParams,
      $q,
      toastr,
      users,
      roles,
      groupOne;

  // Initialize the controller and a mock scope
  beforeEach(inject(function (_$controller_, _dialogs_, _$q_, $rootScope,
      _$httpBackend_, _$location_, _toastr_) {
    $controller = _$controller_;
    dialogs = _dialogs_;
    $httpBackend = _$httpBackend_;
    $location    = _$location_;
    $q = _$q_;
    $routeParams = {};
    scope = $rootScope.$new();
    toastr = _toastr_;
    spyOn(toastr, 'success');

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

    groupOne = {
      "id": 1,
      "name": "Admin",
      "roles": roles,
      "users": users
    };

    $httpBackend.whenGET('/metadata/security/user/me').respond(users[0]);
    $httpBackend.whenGET('/metadata/security/user/myroles').respond([]);
    $httpBackend.whenGET('/metadata/security/group/roles').respond(roles);
    $httpBackend.whenGET('/metadata/security/user').respond(users);
    $httpBackend.whenGET('views/security/login.html').respond();
    $httpBackend.whenGET('views/security/groups.html').respond();
  }));

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  describe('Create Mode', function() {

    beforeEach(function() {

      $routeParams.id = 'create';

      GroupCtrl = $controller('GroupCtrl', {
        $scope: scope,
        $routeParams: $routeParams
      });

      $httpBackend.flush();

    });

    describe('initialization', function() {

      it('should attach a list of users to the scope', function () {
        expect(scope.users['1']).toBeDefined();
        expect(scope.users['1'].id).toEqual(users[0].id);
        expect(scope.users['1'].name).toEqual(users[0].name);
        expect(scope.users['1'].email).toEqual(users[0].email);
        expect(scope.users['1'].enabled).toEqual(users[0].enabled);
        expect(scope.users['1'].groups).toEqual(users[0].groups);
      });

      it('should attach a list of roles to the scope', function () {
        expect(_.size(scope.roles)).toBe(roles.length);
        expect(scope.roles['1'].id).toEqual(roles[0].id);
        expect(scope.roles['1'].description).toEqual(roles[0].description);
      });

      it('should attach a new group to the scope', function() {
        expect(scope.group.name).toEqual("New Group");
        expect(scope.group.roles).toEqual([]);
        expect(scope.group.users).toEqual([]);
      });
    });

    describe('isNewGroup()', function() {
      it('should return true', function() {
        expect(scope.isNewGroup()).toBeTruthy();
      });
    });

    describe('save()', function() {
      it('should send a POST to the server', function() {
        $httpBackend.expectPOST('/metadata/security/group').respond(groupOne);
        scope.save();
        $httpBackend.flush();

        expect(toastr.success).toHaveBeenCalledWith('Group created.');
        expect($location.path()).toBe('/security/groups');
      });
    });
  });

  describe('Edit Mode', function() {
    beforeEach(function() {
      $routeParams.id = groupOne.id;

      $httpBackend.whenGET('/metadata/security/group/' + groupOne.id).respond(groupOne);

      GroupCtrl = $controller('GroupCtrl', {
        $scope: scope
      });

      $httpBackend.flush();
    });

    describe('initialization', function() {
      fit('should attach the group to the scope', function() {
        expect(scope.group.name).toEqual(groupOne.name);
        expect(scope.group.roles).toEqual(groupOne.roles);
        expect(scope.group.users).toEqual(groupOne.users);
      });
    });

    describe('isNewGroup()', function() {
      it('should return false', function() {
        expect(scope.isNewGroup()).toBeFalsy();
      });
    });

    describe('save()', function() {
      it('should send a PUT to the server', function() {
        $httpBackend.expectPUT('/metadata/security/group/' + groupOne.id).respond(groupOne);
        scope.save();
        $httpBackend.flush();

        expect(toastr.success).toHaveBeenCalledWith('Group updated.');
        expect($location.path()).toBe('/security/groups');
      });
    });

    describe('delete()', function() {
      it('should send a DELETE request to the server', function() {

        // Dummy promise for the dialog
        var deferred = $q.defer();
        deferred.resolve();
        spyOn(dialogs, 'confirm').and.returnValue({result: deferred.promise});

        $httpBackend.expectDELETE('/metadata/security/group/' + groupOne.id).respond();
        scope.delete();
        $httpBackend.flush();
      });
    });
  });

});
