'use strict';

angular.module('ngPasswordResetApp', ['ngResource'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/user-verification.html',
                controller: 'UserVerificationCtrl'
            })
            .when('/password-reset', {
                templateUrl: 'views/password-reset.html',
                controller: 'UserVerificationCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
