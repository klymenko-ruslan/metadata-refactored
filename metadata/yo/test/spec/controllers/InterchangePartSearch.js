'use strict';

describe('Controller: InterchangePartSearchCtrl', function () {

    // load the controller's module
    beforeEach(module('ngMetaCrudApp'));

    var InterchangePartSearchCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        InterchangePartSearchCtrl = $controller('InterchangePartSearchCtrl', {
            $scope: scope
        });
    }));

    it('should attach a list of awesomeThings to the scope', function () {
        expect(scope.awesomeThings.length).toBe(3);
    });
});
