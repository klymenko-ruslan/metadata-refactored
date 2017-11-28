'use strict';

angular.module('ngMetaCrudApp')
  .service('utils', [function() {
    function TheService() { // jshint ignore:line

      /**
       * Convert translude to a plain HTML string.
       * Parameters:
       *    transcludeFn - transclude funcntion.
       * Return:
       *    A string with HTML.
       */
      this.transclude2html = function(transcludeFn) {
        var retVal = '';
        transcludeFn(function(clone) {
          clone.each(function(idx, node) {
            if (node.outerHTML) {
              retVal += (node.outerHTML + '\n');
            }
          });
        });
        return retVal;
      };

    }
    return new TheService();
  }]);
