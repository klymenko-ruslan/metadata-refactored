'use strict';

angular.module('ngMetaCrudApp')
    .directive('partSearch', function ($log, restService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: '/views/component/PartSearch.html',
            transclude: true,
            link: function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
              controller.transcludeActionsFn = transcludeFn;
            },
            controller: function ($log, $q, $scope, searchService, ngTableParams) {

              // Query Parameters
              $scope.search = {
                partNumber: "",
                facets: {},
                sort: {}
              };

              // Latest Results
              $scope.searchResults = null;


              // Part Table
              $scope.partTableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {}
              }, {
                getData: function ($defer, params) {

                  // Update the pagination info
                  $scope.search.count = params.count();
                  $scope.search.page = params.page();
                  $scope.search.sorting = params.sorting();

                  searchService($scope.search).then(
                      function (searchResults) {
                        $scope.searchResults = searchResults.data;

                        // Update the total and slice the result
                        $defer.resolve($scope.searchResults.hits.hits);
                        params.total($scope.searchResults.hits.total);
                      },
                      function (errorResponse) {
                        restService.error("Couldn't search for parts.");
                        $defer.reject();
                      });
                }
              });

              // Handle updating search results
              $scope.$watch('[search.partNumber, search.facets]', function (newVal, oldVal) {

                // Debounce
                if (angular.equals(newVal, oldVal, true)) {
                  return;
                }

                $scope.partTableParams.reload();
              }, true);
            }
        };
    })
    .directive('partSearchActions', function($log) {
      return {
        restrict: 'A',
        require: '^partSearch',
        link: function postLink(scope, element, attrs, controller) {
          scope.partId = scope.part._id;
          scope.partType = scope.part._source.partType.typeName;
          controller.transcludeActionsFn(scope, function(clone) {
            element.append(clone);
          });
        }

      }

    });
