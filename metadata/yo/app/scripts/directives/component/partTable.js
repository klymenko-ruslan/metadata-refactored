'use strict';

angular.module('ngMetaCrudApp')
    .directive('partTable', function ($log) {
      return {
        scope: {
          parts: '=',
          key: '@'
        },
        restrict: 'E',
        replace: false,
        transclude: true,
        templateUrl: '/views/component/partTable.html',
        compile: function compile(tElement, tAttrs) {

          // Setup any extra columns
          if (tAttrs.extraColumns) {
            var extraColumns = JSON.parse(tAttrs.extraColumns);

            angular.forEach(extraColumns, function(columnExpression, columnName) {
              tElement.find('thead > tr > th:last').before('<th>' + columnName + '</th>');
              tElement.find('tbody > tr:first > td:last:parent').before('<td>{{' + columnExpression + '}}</td>');
            });

            tElement.find('tbody > tr:last > td').attr('colspan', 4 + _.size(extraColumns));
          }

          return {
            pre: function preLink(scope, iElement, iAttrs, controller) {
            },
            post: function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
              controller.transcludeActionsFn = transcludeFn;
            }
          }
        },
        controller: function($scope) {
          $scope.getPart = function(item) {

            // If we don't have a key, just return the parts list
            if (!$scope.key) {
              return item;
            }

            // We do have a key, extract the part
            return item[$scope.key];
          }
        }
      };
    })
    .directive('partTableActions', function($log) {
      return {
        restrict: 'A',
        require: '^partTable',
        link: function postLink(scope, element, attrs, controller) {

          // Build the scope off partTable's parent
          var newScope = scope.$parent.$parent.$new();
          newScope.index = scope.$index;
          newScope.item = scope.item;
          newScope.part = scope.getPart(scope.item);
          newScope.partId = newScope.part.id;
          newScope.partType = newScope.part.partType.name;

          controller.transcludeActionsFn(newScope, function(clone) {
            element.append(clone);
          });
        }

      }

    });
