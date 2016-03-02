/**
 * Handy function for use together with ngTable.
 * Parameters:
 *    data - array with rows. The array should contain all rows (not only displayed on the current page).
 *    defSortProperty - string, name of a property in a row from 'data' that should be used for sort when
 *                      sorting is not provided by ngTable
 * Return:
 *    Function that can be used as implementation of the function 'getData(params)' in ngTable.
 */
function localPagination(data, defSortProperty) {
  return function(params) {
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
}
