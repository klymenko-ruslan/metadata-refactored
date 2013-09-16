

// Settings controlling ability to edit or edit/delete
$scope.updateDetails = {
    editAuth: true,
    deleteAuth: true
};

// Routing function with destination as its variable in angular app/string format
$scope.go = function (path) {
    $location.path(path);
};

// Launches modal updater window
$scope.launchUpdater = function () {
    var title = 'This is the updater window';
    var msg = 'This is the content of the updater window';
    var btns = [
        {result: 'updated', label: 'Update', cssClass: 'btn-primary'},
        {result: 'canceled', label: 'Cancel'}
    ];

//            $dialog.messageBox(title, msg, btns)
//                .open()
//                .then(function (result) {
//                    console.log('Dialog closed with result: ' + result);
//                });
};

// Deletes a part, more specifically an item in bom
$scope.deletePart = function () {
    console.log('I\'m deleting a part!');
};
