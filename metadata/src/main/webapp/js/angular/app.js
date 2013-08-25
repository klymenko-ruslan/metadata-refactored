var MetadataEditApp = angular.module('MetadataEditApp', ['ngResource', 'ngTable']);

MetadataEditApp.factory('partSearchService', function($http) {
    return function(path, data, successCallback, errorCallback) {
        return $http({method: 'POST', url: path, params: data})
            .success(successCallback)
            .error(errorCallback);
    }
});

MetadataEditApp.directive('myInterchanges', function () {
    return {
        scope: {
            interchangeId: '@',
            path: '@'
        },
        restrict: 'E',
        transclude: true,
        templateUrl:'/partials/Interchanges.html',
        controller: 'InterchangesCtrl',
        link: function(scope, element, attrs, controller) {
            scope.interchangeId = attrs.interchangeId;
            scope.interchangeNewId = attrs.interchangeId;
        }
    };
});

MetadataEditApp.directive('myPartSearch', function () {
    return {
        scope: {
            path: '@',
            ngModel: '='
        },
        restrict: 'E',
        require: "ngModel",
        templateUrl:'/partials/PartSearch.html',
        controller: 'PartSearchCtrl'
    };
});

MetadataEditApp.controller('InterchangesCtrl', function($scope, $resource) {

    // Values (set in the linker)
    $scope.interchangeId;
    $scope.interchangeNewId;

    // Methods
    $scope.isChanged = function() {
        return $scope.interchangeId !== $scope.interchangeNewId;
    };

    $scope.undo = function() {
        $scope.interchangeNewId = $scope.interchangeId;
        $scope.interchangePartId = null;
    };

    $scope.clear = function() {
        $scope.interchangeNewId = null;
        $scope.interchangePartId = null;
    };

    $scope.pick = function(part) {
        alert("PICK");
        $scope.interchangeNewId = part.interchange_id;
        $scope.interchangePartId = part._id;
    };
    
    $scope.$watch('result', function() {
        if (typeof $scope.result !== 'undefined') {
            $scope.interchangeNewId = $scope.result.interchange_id
            $scope.interchangePartId = $scope.result._id
        }
    })
});

MetadataEditApp.controller('PartSearchCtrl', function($scope, ngTableParams, partSearchService) {
    
    // Values
    $scope.isSearching = false; 
    $scope.query = "43";

    $scope.tableParams = new ngTableParams({
         size:20,
         page:1,
         total:0,
         counts: []
    });

    $scope.search = function () {

       // Clear the results
       $scope.tableParams.page = 1;
       $scope.tableParams.total = 0;
       $scope.searchResults = [];

        if ($scope.query && $scope.query.length >=2) {
           $scope.isSearching = true;

           partSearchService($scope.path, {
               query:$scope.query,
               from:($scope.tableParams.page-1) * $scope.tableParams.size,
               size:$scope.tableParams.size
             }, function(data) {
               $scope.isSearching = false;
               $scope.tableParams.total = data.total;
               $scope.searchResults = data.items;
             }, function(data) {
               $scope.isSearching = false;
               alert("error:" + data);
             });
        } else {
            $scope.isSearching = false;
        }
    };
    
    $scope.pick = function(result) {
        $scope.ngModel = result;
    }

    // Watchers
    $scope.$watch('query', $scope.search, true);
    $scope.$watch('tableParams.page', $scope.search, true);
});
