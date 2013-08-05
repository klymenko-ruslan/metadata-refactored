'use strict';

describe('Controller: UserVerificationCtrl', function () {

    // load the controller's module
    beforeEach(module('ngPasswordResetApp'));

    var UserVerificationCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        UserVerificationCtrl = $controller('UserVerificationCtrl', {
            $scope: scope
        });
    }));

    it('should attach a list of awesomeThings to the scope', function () {
        expect(scope.awesomeThings.length).toBe(3);
    });
});
