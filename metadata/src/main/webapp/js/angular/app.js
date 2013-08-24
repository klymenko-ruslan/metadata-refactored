var MetadataEditApp = angular.module('MetadataEditApp', ['ngResource', 'ngTable']);

MetadataEditApp.directive('myInterchanges', function () {
    return {
        scope: {
            interchangeId: '='
        },
        restrict: 'E',
        transclude: true,
        templateUrl:'/partials/Interchanges.html',
        controller: 'InterchangesCtrl'
    };
});

MetadataEditApp.directive('myPartSearch', function () {
    return {
        scope: {
            path: '='
        },
        restrict: 'E',
        replace: true,
        transclude: true,
        templateUrl:'/partials/PartSearch.html',
        controller: 'PartSearchCtrl'
    };
});

MetadataEditApp.controller('PartSearchCtrl', function($scope, $resource, ngTableParams) {

    // Resources
    var Part = $resource($scope.path, {}, {
        search: {
            method:'GET',
            params: {
                query:'@query'
            }
        }
    });

    // Values
    $scope.isSearching = false;
    $scope.query = "";
    $scope.callback = null;

    $scope.tableParams = new ngTableParams({
         size:20,
         page:1,
         total:0,
         counts: []
    });

    $scope.search = function (params) {

       // Clear the results
       $scope.tableParams.page = 1;
       $scope.tableParams.total = 0;
       $scope.searchResults = [];

        if ($scope.query && $scope.query.length >=2) {
           $scope.isSearching = true;

           Part.search({
               query:$scope.query,
               from:($scope.tableParams.page-1) * $scope.tableParams.size,
               size:$scope.tableParams.size
             }, function(data) {
               $scope.isSearching = false;
               $scope.tableParams.total = data.total;
               $scope.searchResults = data.items;
           });
        }
    };

    // Watchers
    $scope.$watch('query', $scope.search, true);
    $scope.$watch('tableParams.page', $scope.search, true);
});


MetadataEditApp.controller('InterchangesCtrl', function($scope, $resource, ngTableParams) {

    // Values
    $scope.interchangeOldId = $scope.interchangeId;

    // Methods
    $scope.isChanged = function() {
        return $scope.interchangeId !== $scope.interchangeOldId;
    };

    $scope.undo = function() {
        $scope.interchangeId = $scope.interchangeOldId;
        $scope.interchangePartId = null;
    };

    $scope.clear = function() {
        $scope.interchangeId = null;
        $scope.interchangePartId = null;
    };

    $scope.pick = function(part) {
        $scope.interchangeId = part.interchange_id;
        $scope.interchangePartId = part._id;
    };
});