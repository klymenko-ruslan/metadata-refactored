'use strict';

describe('Controller: UsersCtrl', function () {

  // load the controller's module
  beforeEach(module('ngMetaCrudApp'));

  var scope, restService, foo;

  // Initialize the controller and a mock scope
  beforeEach(inject(function (_$controller_, _NgTableParams_, _$q_) {

    scope = {
    };

    restService = {
        filterUsers: function() {
          return _$q_(function(resolve) {
            resolve({
            "total": 3,
            "recs": [
              {
                 "id": 19,
                 "name": "Alex Jimenez",
                 "email": "ajimenez@turbointernational.com",
                 "username": "ajimenez",
                 "enabled": true,
                 "authProvider": null,
                 "groups": [
                   {
                      "id": 1,
                      "name": "Reader"
                   },
                   {
                      "id": 7,
                      "name": "Sales Notes User"
                   },
                   {
                      "id": 8,
                      "name": "Sales Writer"
                   }
                 ]
              },
              {
                 "id": 4,
                 "name": "Brian Malewicz",
                 "email": "bmalewicz@dbicapital.com",
                 "username": "bmalewicz@dbicapital.com",
                 "enabled": true,
                 "authProvider": null,
                 "groups": [
                   {
                      "id": 2,
                      "name": "Writer"
                   },
                   {
                      "id": 4,
                      "name": "Sales Notes Editor"
                   },
                   {
                      "id": 6,
                      "name": "MAS90 Sync"
                   }
                 ]
              },
              {
                 "id": 10001,
                 "name": "Coby Reddick",
                 "email": "creddick@turbointernational.com",
                 "username": "creddick",
                 "enabled": true,
                 "authProvider": {
                             "id": 2,
                             "typ": "LDAP",
                             "name": "TurboInternational AD (LDAPS SOFT)",
                             "host": "ldap.turbointernational.com",
                             "port": 636,
                             "protocol": "LDAPS_SOFT",
                             "domain": "turbointernational.local"
                           },
                 "groups": []
               }
            ]
          })});
        }
    };

    spyOn(restService, 'filterUsers').and.callThrough();

    _$controller_('UsersCtrl', {
      $log: jasmine.createSpyObj('$log', ['log']),
      $scope: scope,
      NgTableParams: _NgTableParams_,
      restService: restService,
      authProviders: {
        "total": 2,
        "recs": [
          {
            "id": 1,
            "typ": "LDAP",
            "name": "TurboInternational AD (LDAP)",
            "host": "ldap.turbointernational.com",
            "port": 389,
            "protocol": "LDAP",
            "domain": "turbointernational.local"
          },
          {
            "id": 2,
            "typ": "LDAP",
            "name": "TurboInternational AD (LDAPS SOFT)",
            "host": "ldap.turbointernational.com",
            "port": 636,
            "protocol": "LDAPS_SOFT",
            "domain": "turbointernational.local"
          }
        ]
      }
    });

  }));

  it('should attach a list of authentication providers to the scope', function () {
    expect(scope.authProviders).toBeTruthy();
    expect(scope.authProviders.length).toBe(4);
    expect(scope.authProviders).toContain({id : -1, title: ''});
    expect(scope.authProviders).toContain({id : null, title: 'Local DB'});
    expect(scope.authProviders).toContain({id : 1,
      title: 'TurboInternational AD (LDAP)'});
    expect(scope.authProviders).toContain({id : 2,
      title: 'TurboInternational AD (LDAPS SOFT)'});
  });

  it('should initialize ng-table parameters correctly', function () {
    expect(scope.usersTableParams).toBeTruthy();
    var parameters = scope.usersTableParams.parameters();
    expect(parameters).toBeTruthy();
    expect(1).toBe(parameters.page);
    expect(25).toBe(parameters.count);
  });

  it('should initialize ng-table sorting correctly', function () {
    expect(scope.usersTableParams).toBeTruthy();
    expect(true).toBe(scope.usersTableParams.isSortBy('name', 'asc'));
  });

  it('should call RESTful method with correct parameters to load users', function () {
    expect(scope.usersTableParams).toBeTruthy();
    scope.usersTableParams.reload();
    expect(restService.filterUsers).toHaveBeenCalled();
  });

  it('should initialize ng-table filter correctly', function () {
    expect(scope.usersTableParams).toBeTruthy();
    var filter = scope.usersTableParams.filter();
    expect(null).toBe(filter.displayName);
    expect(null).toBe(filter.userName);
    expect(null).toBe(filter.email);
    expect(null).toBe(filter.enabled);
    expect(-1).toBe(filter.authProviderId);
  });

  it('clear filter', function () {
    var filter = scope.usersTableParams.filter();
    expect(filter).toBeTruthy();
    filter.displayName = 'Hello world!';
    filter.userName = 'John';
    filter.email = 'foo@google.com';
    filter.enabled = true;
    filter.authProviderId = 1;
    scope.clearFilter();
    expect(null).toBe(filter.displayName);
    expect(null).toBe(filter.userName);
    expect(null).toBe(filter.email);
    expect(null).toBe(filter.enabled);
    expect(-1).toBe(filter.authProviderId);
  });

});
