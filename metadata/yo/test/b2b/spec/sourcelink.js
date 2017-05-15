'use strict';

describe('navigation menu', function() {

  var cntrlLogin, cntrlLogout;

  beforeAll(function () {
    cntrlLogin = element(by.id('bttn-login'));
    cntrlLogout = element(by.id('lnk-logout'))
    // Login.
    browser.get('http://localhost:8080');
    element(by.id('username')).sendKeys('pavlo.kurochka@zorallabs.com');
    element(by.id('password')).sendKeys('zoraltemp');
    cntrlLogin.click();
  });

  afterAll(function () {
    // Logout.
    cntrlLogout.click();
  });

  /**
   * Check that navigation menu is clickable.
   * The test goal looks trivial and useless, but it is not so.
   * I encoured with situation when it was unclickable after
   * bootstrap upgrade.
   */
  it('should link source with file uploads', function() {
    browser.get('http://localhost:8080/part/14510/bom/search');
    var cntrlFltrPart = element(by.model('fltrPart.manufacturer'));
    cntrlFltrPart.$('[value=Holset]').click();
  });

});

