'use strict';

angular.module('ngPasswordResetApp')
    .controller('UserVerificationCtrl', function ($scope, $resource, $http) {

        $scope.resetPassword = function (email) {
            $http.http('http://localhost:8080/password-reset.do', {email:email})
                .success(function(data, status, headers, config) {
                    if (data.result == "ok") {
                        alert(data.url);
                    } else {
                        alert(data.reason);
                    }
                });
        }
    });
