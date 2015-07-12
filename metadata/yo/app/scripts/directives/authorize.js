'use strict';

angular.module('ngMetaCrudApp')
  .directive('authorize', function ($log, User) {
    return {
      restrict: 'A',
      link: function postLink(scope, element, attrs) {
        var role = attrs.authorize;

        scope.$watch(function() {return User.roles;}, function() {
          if (_.contains(User.roles, role)) {
            element.removeClass('hidden');
          } else {
            element.addClass('hidden');
          }
        }, true);
      }
    };
  });
