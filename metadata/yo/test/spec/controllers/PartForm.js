'use strict';

describe('Controller: PartFormCtrl', function () {

    // load the controller's module
    beforeEach(module('ngMetaCrudApp'));

    var PartFormCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        PartFormCtrl = $controller('PartFormCtrl', {
            $scope: scope
        });
    }));

    it('should attach a list of awesomeThings to the scope', function () {
        expect(scope.awesomeThings.length).toBe(3);
    });
});
