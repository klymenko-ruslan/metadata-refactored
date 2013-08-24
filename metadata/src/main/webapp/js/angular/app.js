var MetadataApp = angular.module('MetadataApp', ['ngResource', 'ngTable']);

MetadataApp.directive('myPartSearch', function () {
    return {
        scope: {
        },
        templateUrl:'/partials/PartSearch.html'
    };
});

MetadataApp.controller('PartSearchCtrl', function($scope, $resource, ngTableParams) {

    // Resources
    var Part = $resource('${path}', {}, {
        search: {
            method:'GET',
            params: {
                query:'@query',

            }
        }
    });

    // Values
    $scope.isSearching = false;
    $scope.query = "";
    $scope.callback = null;

    $scope.tableParams = new ngTableParams({
         size:${pageSize},
         page:1,
         total:0,
         counts: []
    });

    $scope.search = function (params) {

       // Clear the results
       $scope.tableParams.page = 1;
       $scope.tableParams.total = 0;
       $scope.searchResults = [];

        if ($scope.query ${'&amp;&amp;'} $scope.query.length >=2) {
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


MetadataApp.controller('InterchangesCtrl', function($scope, $resource, ngTableParams) {

    // Values
    $scope.interchangeId = ${part.interchange.id != null ? part.interchange.id : "null"};
    $scope.interchangeOldId = ${part.interchange.id != null ? part.interchange.id : "null"};
    $scope.interchangePartId = null;

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

    $scope.pick = function(params) {
        $scope.interchangeId = params.interchange_id;
        $scope.interchangePartId = params._id;
    };
});