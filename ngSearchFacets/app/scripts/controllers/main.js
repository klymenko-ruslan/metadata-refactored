'use strict';

angular.module('ngSearchFacetsApp')
  .controller('MainCtrl', function ($scope, ejsResource) {
    $scope.ejs = ejsResource('http://bigcommerce:mvgctvfxo0ha9fezrtpgjfhwahmxeejz@api.searchbox.io');
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
