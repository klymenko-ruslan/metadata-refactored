'use strict';

angular.module('ngMetaCrudApp')
    .controller('MainCtrl', function ($scope, $location, Restangular) {

        // Example JSON Part data
        $scope.bom = [
            {
                id: 45408,
                name: "Gasket, oil inlet",
                mfr: "Turbo International",
                mfrpn: "8-A-0062",
                quantity: 1,
                image: "images/parts/turbo.jpg"
            },
            {
                id: 45409,
                name: "Gasket",
                mfr: "Turbo International",
                mfrpn: "8-A-0070",
                quantity: 1,
                image: "images/parts/turbo.jpg"
            },
            {
                id: 45410,
                name: "Gasket, Narrow",
                mfr: "Turbo International",
                mfrpn: "8-A-0071",
                quantity: 1,
                image: "images/parts/turbo.jpg"
            },
            {
                id: 45415,
                name: "Gasket",
                mfr: "Turbo International",
                mfrpn: "8-A-0710",
                quantity: 1,
                image: "images/parts/turbo.jpg"
            },
            {
                id: 45433,
                name: "Gasket, Wide",
                mfr: "Turbo International",
                mfrpn: "8-A-9999",
                quantity: 1,
                image: "images/parts/turbo.jpg"
            }
        ];

        $scope.partTypes = [
            {
                type: "Turbines"
            },
            {
                type: "Turbo Kits"
            },
            {
                type: "Backplates"
            },
            {
                type: "Gaskets"
            }
        ];

        // Routing function with destination as its variable in angular app/string format
        $scope.go = function (path) {
            $location.path(path);
        };

        // First way of creating a Restangular object. Just saying the base URL
        var baseParts = Restangular.all('parts');

        // This will query /parts and return a promise. As Angular supports setting promises to scope variables
        // as soon as we get the information from the server, it will be shown in our template :)
        $scope.allParts = baseParts.getList();

        var newPart = {name: "Turbo Kit Alpha"};

        // POST /parts
        baseParts.post(newPart);

        // GET to http://www.google.com/ You set the URL in this case
        Restangular.allUrl('toParts', 'parts').getList();

        // GET to http://www.google.com/1 You set the URL in this case
        Restangular.oneUrl('toParts', 'parts/part').get();

        // You can do RequestLess "connections" if you need as well

        // Just ONE GET to /parts/123/types/456
        Restangular.one('parts', 123).one('types', 456).get();

        // Just ONE GET to /parts/123/types
        Restangular.one('parts', 123).getList('types');

        // Here we use Promises then
        // GET /parts
        baseParts.getList().then(function (parts) {
            // Here we can continue fetching the tree :)
            var firstPart = parts[0];
            // This will query /parts/123/types considering 123 is the id of the firstPart
            $scope.types = firstPart.getList("types");

            // GET /parts/123/manufacturers?query=param with request header: x-name:Gasket, oil inlet
            $scope.loggedInManufacturers = firstPart.getList("manufacturers", {query: param}, {'x-name': 'Gasket, oil inlet'});

            // This is a regular JS object, we can change anything we want :)
            firstPart.name = "Gasket, oil inlet";

            // If we wanted to keep the original as it is, we can copy it to a new element
            var editFirstPart = Restangular.copy(firstPart);
            editFirstPart.name = "New Name";


            // PUT /parts/123. The name of this part will be changed from now on
            firstPart.put();
            editFirstPart.put();

            // DELETE /parts/123 We don't have first part anymore :(
            firstPart.remove();

            var myType = {
                name: "Alfred's Type",
                manufacturer: "Billet"
            };

            // POST /parts/123/types with myType information
            firstPart.post("types", myType).then(function () {
                console.log("Object saved OK");
            }, function () {
                console.log("There was an error saving");
            });

            // GET /parts/123/users?query=params
            firstPart.getList("users", {query: params}).then(function (users) {
                // Instead of posting nested element, a collection can post to itself
                // POST /parts/123/users
                users.post({userName: 'unknown'});

                // Custom methods are available now :)
                // GET /parts/123/users/messages?param=myParam
                users.customGET("messages", {param: "myParam"});

                var firstUser = users[0];

                // GET /parts/123/users/456. Just in case we want to update one user :)
                $scope.userFromServer = firstUser.get();

                // ALL http methods are available :)
                // HEAD /parts/123/users/456
                firstUser.head()

            });

        }, function errorCallback() {
            console.log("Oops error from server :(");
        });

        // Second way of creating Restangular object. URL and ID :)
        var Part = Restangular.one("parts", 123);

        // GET /parts/123?single=true
        $scope.Part = Part.get({single: true});

        // POST /parts/123/messages?param=myParam with the body of name: "My Message"
        Part.customPOST("messages", {param: "myParam"}, {}, {name: "My Message"})
    });