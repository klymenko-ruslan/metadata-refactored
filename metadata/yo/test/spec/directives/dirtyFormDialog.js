'use strict';

describe('Directive: dirtyFormDialog', function () {

  // load the directive's module
  beforeEach(module('ngMetaCrudApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<dirty-form-dialog></dirty-form-dialog>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the dirtyFormDialog directive');
  }));
});
