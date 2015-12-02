'use strict';

angular.module('ngMetaCrudApp')
    .directive('applicationSearch', function ($log, restService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: '/views/component/partApplicationSearch.html',
            transclude: true,
            link: function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
              controller.transcludeActionsFn = transcludeFn;
            },
            controller: function ($log, $q, $scope, applicationSearchService, ngTableParams) {

              // Latest Results
              $scope.searchResults = null;

              // Applications Table
              $scope.applicationTableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {}
              }, {
                getData: function ($defer, params) {

                  // Update the pagination info
                  $scope.search.count = params.count();
                  $scope.search.page = params.page();
                  $scope.search.sorting = params.sorting();

                  applicationSearchService($scope.search).then(
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
                application: "",
                facets: {},
                sort: {}
              };

              $scope.clear = function() {
                $scope.search = {
                  application: "",
                  facets: {},
                  sort: {}
                }
              }

              // Handle updating search results
              $scope.$watch('[search.application, search.facets]', function (newVal, oldVal) {

                // Debounce
                if (angular.equals(newVal, oldVal, true)) {
                  return;
                }

                $scope.applicationTableParams.reload();
              }, true);
            }
        };
    })
    .directive('applicationSearchActions', function($log) {
      return {
        restrict: 'A',
        require: '^applicationSearch',
        link: function postLink(scope, element, attrs, controller) {
          controller.transcludeActionsFn(scope, function(clone) {
            element.append(clone);
          });
        }

      }

    });
