'use strict';

describe('Controller: TurboModelCtrl', function () {

    // load the controller's module
    beforeEach(module('ngMetaCrudApp'));

    var TurboModelCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        TurboModelCtrl = $controller('TurboModelCtrl', {
            $scope: scope
        });
    }));

    it('should attach a list of awesomeThings to the scope', function () {
        expect(scope.awesomeThings.length).toBe(3);
    });
});
