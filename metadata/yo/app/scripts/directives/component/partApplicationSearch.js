'use strict';

angular.module('ngMetaCrudApp')
    .directive('cmeySearch', function ($log, restService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: '/views/component/partApplicationSearch.html',
            transclude: true,
            link: function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
              controller.transcludeActionsFn = transcludeFn;
            },
            controller: function ($log, $q, $scope, cmeySearchService, ngTableParams) {

              // Latest Results
              $scope.searchResults = null;

              // Applications Table
              $scope.cmeyTableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {}
              }, {
                getData: function ($defer, params) {
                  // Update the pagination info
                  $scope.search.count = params.count();
                  $scope.search.page = params.page();
                  $scope.search.sorting = params.sorting();

                  cmeySearchService($scope.search).then(
                      function (searchResults) {
                        $scope.searchResults = searchResults.data;

                        // Update the total and slice the result
                        $defer.resolve($scope.searchResults.hits.hits);
                        params.total($scope.searchResults.hits.total);
                      },
                      function (errorResponse) {
                        $log.log("Couldn't search for applications.");
                        $defer.reject();
                      });
                }
              });

              // Query Parameters
              $scope.search = {
                cmey: "",
                facets: {},
                sort: {}
              };

              $scope.clear = function() {
                $scope.search = {
                  cmey: "",
                  facets: {},
                  sort: {}
                }
              }

              // Handle updating search results
              $scope.$watch('[search.cmey, search.facets]', function (newVal, oldVal) {

                // Debounce
                if (angular.equals(newVal, oldVal, true)) {
                  return;
                }

                $scope.cmeyTableParams.reload();
              }, true);
            }
        };
    })
    .directive('cmeySearchActions', function($log) {
      return {
        restrict: 'A',
        require: '^cmeySearch',
        link: function postLink(scope, element, attrs, controller) {
          controller.transcludeActionsFn(scope, function(clone) {
            element.append(clone);
          });
        }

      }

    });
