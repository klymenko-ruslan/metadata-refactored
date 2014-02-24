'use strict';

angular.module('ngMetaCrudApp')
    .directive('partSearch', function ($log) {
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
                queryString: "",
                facetFilters: {}
              };

              // Latest Results
              $scope.searchResults = null;

              $scope.partTableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {
                  manufacturerPartNumber: 'asc'
                }
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
                        alert("Could not complete search");
                        $log.log("Could not complete search", errorResponse);
                        $defer.reject();
                      });
                }
              });

              // Handle updating search results
              $scope.$watch('search', function () {
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
