'use strict';

angular.module('ngMetaCrudApp')
    .controller('BillOfMaterialsCtrl', function ($scope, ngTableParams) {
        $scope.bomTableParams = new ngTableParams({

        });

        $scope.bom = [
            {
                id: 45408,
                name: "Gasket, oil inlet",
                mfr: "Turbo International",
                mfrpn: "8-A-0062",
                quantity: 1
            },
            {
                id: 45409,
                name: "Gasket",
                mfr: "Turbo International",
                mfrpn: "8-A-0070",
                quantity: 1
            },
            {
                id: 45410,
                name: "Gasket, Narrow",
                mfr: "Turbo International",
                mfrpn: "8-A-0071",
                quantity: 1
            },
            {
                id: 45415,
                name: "Gasket",
                mfr: "Turbo International",
                mfrpn: "8-A-0710",
                quantity: 1
            },
        ];

        $scope.$on("PartTable.click", function (part) {
            console.log("Broadcast: " + JSON.stringify(part));
            bom.push({
                id: part._id,
                mfr: part._source.manufacturer_name,
                mfrpn: part._source.manufacturer_part_number,
                name: part._source.name,
                quantity: 1
            });
        });
    });
