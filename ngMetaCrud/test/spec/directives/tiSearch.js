'use strict';

describe('Directive: tiSearch', function () {

  // load the directive's module
  beforeEach(module('ngMetaCrudApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<ti-search></ti-search>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the tiSearch directive');
  }));
});
