'use strict';

angular.module('ngMetaCrudApp')
.directive('resetBttn', ['$parse', function($parse) {
    return {
      restrict: 'E',
      templateUrl: '/views/component/resetbttn.html',
      link: function (scope, element, attrs) {
        if (attrs.action) {
          var action = $parse(attrs.action);
          element.on('click', function() {
            scope.$eval(action)
          });
        }
      },
      controller: ['$scope',
        function($scope) {
        }
      ]
    };
  }
]);
