'use strict';

angular.module('ngMetaCrudApp')
    .directive('bomStatus', function(restService) {
      return {
        restrict: 'EA',
        transclude: true,
        template: '<div class="alert alert-warning"> \
                     <i class="fa fa-cog fa-spin"></i> \
                     Rebuilding BOM Ancestry\
                     <div ng-transclude></div> \
                   </div>',
        link: function postLink(scope, element, attrs) {
          
          // Hidden by default
          angular.element(element).addClass('hidden');
          
          restService.refreshStatus().finally(function() {
            scope.$watch(
                function() {
                  return restService.status;
                },
                function(status) {
                  if (status.bomAncestryRebuilding === true) {
                    angular.element(element).removeClass('hidden');
                  } else {
                    angular.element(element).addClass('hidden');
                  }
                }, true);
          });
        }
      };
    });
