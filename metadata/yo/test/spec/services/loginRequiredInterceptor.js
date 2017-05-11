'use strict';

describe('Service: loginRequiredInterceptor', function () {

  // load the service's module
  beforeEach(module('ngMetaCrudApp'));

  // instantiate service
  var loginRequiredInterceptor,
      $location,
      $httpBackend,
      Restangular;
      
  beforeEach(inject(function (_loginRequiredInterceptor_, _$location_, 
    _$httpBackend_, _Restangular_) {

    loginRequiredInterceptor = _loginRequiredInterceptor_;
    $location = _$location_;
    $httpBackend = _$httpBackend_;
    Restangular = _Restangular_;

    $location.path('/foo');
  }));

  it('should redirect to / on a 401 status', function () {
    expect($location.path()).toEqual('/foo'); // check initial state
    $httpBackend.expectGET('/metadata/security/user/me').respond(401);
    Restangular.one('security/user/me').get();
    $httpBackend.flush();
    expect($location.path()).toEqual('/');
  });

  it('should redirect to / on a 403 status', function () {
    expect($location.path()).toEqual('/foo'); // check initial state
    $httpBackend.expectGET('/metadata/security/user/me').respond(403);
    Restangular.one('security/user/me').get();
    $httpBackend.flush();
    expect($location.path()).toEqual('/');
  });

  xit('should allow do nothing otherwise', function () {
    expect($location.path()).toEqual('/foo'); // check initial state
    $httpBackend.expectGET('/metadata/security/user/me').respond(200);
    Restangular.one('security/user/me').get();
    $httpBackend.flush();
    expect($location.path()).toEqual('/foo');
  });

});
