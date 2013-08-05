'use strict';

angular.module('jeffwessonApp', [])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/about.html',
                controller: 'AboutCtrl'
            })
            .when('/resume.html', {
                templateUrl: 'views/resume.html',
                controller: 'ResumeCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
