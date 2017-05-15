'use strict';

describe('navigation menu', function() {

  var cntrlLogin, cntrlLogout;

  beforeAll(function () {
    cntrlLogout = element(by.id('lnk-logout'))
    cntrlLogin = element(by.id('bttn-login'));
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
  it('should open menu', function() {
    var cntrlEntities = element(by.id('nav-lnk-entities'));
    expect(cntrlEntities.isDisplayed()).toBeTruthy();
    var cntrlPartTypes = element(by.id('nav-lnk-parttypes'));
    expect(cntrlPartTypes.isDisplayed()).toBeFalsy();
    cntrlEntities.click();
    expect(cntrlPartTypes.isDisplayed()).toBeTruthy();
  });

});

