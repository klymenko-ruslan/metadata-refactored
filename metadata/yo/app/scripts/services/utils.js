"use strict";

angular.module("ngMetaCrudApp")
  .service("utils", ["$parse", function RestService($parse) {
    return new function() { // jshint ignore:line

      /**
       * Handy function for use together with ngTable.
       * Parameters:
       *    data - array with rows. The array should contain all rows (not only displayed on the current page).
       *    defSortProperty - string, name of a property in a row from 'data' that should be used for sort when
       *                      sorting is not provided by ngTable
       * Return:
       *    Function that can be used as implementation of the function 'getData(params)' in ngTable.
       */
      this.localPagination = function(data, defSortProperty) {
        return function(params) {
          if (!angular.isObject(data)) {
            return null;
          }
          var sorting = params.sorting();
          var sortAsc = true;
          for (var sortProperty in sorting) break;
          if (sortProperty) {
            sortAsc = sorting[sortProperty] == "asc";
          } else {
            sortProperty = defSortProperty; // asc. see above.
          }
          var sortedAsc = _.sortBy(data, function(b) {
            var s = $parse(sortProperty)(b);
            if (s && _.isString(s)) {
              s = s.toLowerCase();
            }
            return s;
          });
          var sorted = sortAsc ? sortedAsc : sortedAsc.reverse();
          var page = sorted.slice((params.page() - 1) * params.count(), params.page() * params.count());
          params.total(data.length);
          return page;
        };
      };

      /**
       * Convert translude to a plain HTML string.
       * Parameters:
       *    transcludeFn - transclude funcntion.
       * Return:
       *    A string with HTML.
       */
      this.transclude2html = function(transcludeFn) {
        var retVal = "";
        transcludeFn(function(clone) {
          clone.each(function(idx, node) {
            if (node.outerHTML) {
              retVal += (node.outerHTML + "\n");
            }
          });
        });
        return retVal;
      };

    };
  }]);
