'use strict';

angular.module('jeffwessonApp')
    .controller('AboutCtrl', function ($scope) {
        $scope.views = [
            {
                'name': 'About',
                'title': 'A bit about myself'
            },
            {
                'name': "R&eacute;sum&eacute;",
                'title': "R&eacute;sum&eacute;"
            },
            {
                'name': 'Contact',
                'title': 'How to get in touch'
            }
        ];
    });
