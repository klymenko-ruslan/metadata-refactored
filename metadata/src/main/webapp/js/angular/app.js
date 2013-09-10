var MetadataEditApp = angular.module('MetadataEditApp', ['ngResource', 'ngTable']);

MetadataEditApp.factory('partSearchService', function($http) {
    return function(path, data, successCallback, errorCallback) {
        return $http({method: 'POST', url: path, params: data})
            .success(successCallback)
            .error(errorCallback);  
    }
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

MetadataEditApp.controller('PartSearchCtrl', function($scope, ngTableParams, partSearchService) {
    
    // Values
    $scope.isSearching = false; 
    $scope.query = "43";

    $scope.tableParams = new ngTableParams({
         count:20,
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

MetadataEditApp.controller('ModalCtrl', function($scope, createDialog) {
    $scope.items = [
        {name: 'value1'},
        {name: 'value2'},
        {name: 'value3'}
    ];
    $scope.launchModal = function() {
        console.log("Hello");
        createDialog('/partials/Modal.html', { 
            id : 'modal-window', 
            title: 'Modal Window',
            backdrop: true, 
            success: {label: 'Search', fn: function() {
                console.log("Can you see me?");
            }},
            controller: 'ModalCtrl', 
            backdropClass: 'modal-backdrop', 
            /* footerTemplate: [modal_footer_template], */ 
            modalClass: 'modal' 
        }, {name: 'value4'});
    }
});
