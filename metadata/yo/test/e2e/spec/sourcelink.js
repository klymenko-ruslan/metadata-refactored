'use strict';

var _ = require('underscore');

describe('Navigation menu:', function() {

  var cntrlLogin, cntrlLogout;

  beforeAll(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/part/14510/bom/search');
  });

  it('should link source with file uploads', function() {
    expect(browser.getCurrentUrl())
      .toEqual('http://localhost:8080/part/14510/bom/search');
    element(by.id('fltrManufacturer')).all(by.tagName('option'))
      .each(function(opt, idx) {
        //console.log(idx + ' - ' + opt.getId());
      });
  });

});

