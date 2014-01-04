'use strict';

describe('Controller: PartTableCtrl', function () {

    // load the controller's module
    beforeEach(module('ngMetaCrudApp'));

    var PartTableCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        PartTableCtrl = $controller('PartTableCtrl', {
            $scope: scope
        });
    }));

    it('should attach a list of awesomeThings to the scope', function () {
        expect(scope.awesomeThings.length).toBe(3);
    });
});
