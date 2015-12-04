'use strict';

describe('Directive: salesNoteActions', function () {

  // load the directive's module
  beforeEach(module('ngMetaCrudApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<sales-note-actions></sales-note-actions>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the salesNoteActions directive');
  }));
});
