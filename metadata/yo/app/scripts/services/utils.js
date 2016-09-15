"use strict";

angular.module("ngMetaCrudApp")
  .service("utils", ["$log", "$parse", function RestService($log, $parse) {
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
          if (data.length === 0) {
            return data;
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
          var currentPageIdx = params.page() - 1;
          var begin = currentPageIdx * params.count();
          if (begin < 0) {
            begin = 0;
            currentPageIdx = 0;
          } else if (begin >= data.length) {
            // Find the last page.
            currentPageIdx = Math.floor((data.length - 1) / params.count());
            begin = currentPageIdx * params.count();
          }
          var end = begin + params.count();
          if (end > data.length) {
            end = data.length;
          }
          if (params.page() - 1 != currentPageIdx) {
            params.page(currentPageIdx);
          }
          var pageRows = sorted.slice(begin, end);
          params.total(data.length);
          return pageRows;
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
