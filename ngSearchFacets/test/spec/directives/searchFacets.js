'use strict';

describe('Directive: searchFacets', function () {

  // load the directive's module
  beforeEach(module('ngSearchFacetsApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<search-facets></search-facets>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the searchFacets directive');
  }));
});
