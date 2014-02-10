'use strict';

describe('Controller: PartEditCtrl', function () {

    // load the controller's module
    beforeEach(module('ngMetaCrudApp'));

    var PartEditCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        PartEditCtrl = $controller('PartEditCtrl', {
            $scope: scope
        });
    }));

    it('should attach a list of awesomeThings to the scope', function () {
        expect(scope.awesomeThings.length).toBe(3);
    });
});
