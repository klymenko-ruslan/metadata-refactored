'use strict';

describe('Controller: PartCreateModalCtrl', function () {

    // load the controller's module
    beforeEach(module('ngMetaCrudApp'));

    var PartCreateModalCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        PartCreateModalCtrl = $controller('PartCreateModalCtrl', {
            $scope: scope
        });
    }));

    it('should attach a list of awesomeThings to the scope', function () {
        expect(scope.awesomeThings.length).toBe(3);
    });
});
