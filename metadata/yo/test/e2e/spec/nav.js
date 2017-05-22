'use strict';

describe('Navigation menu:', function() {

  beforeAll(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/part/list');
  });

  /**
   * Check that navigation menu is clickable.
   * The test goal looks trivial and useless, but it is not so.
   * I encoured with situation when it was unclickable after
   * bootstrap upgrade.
   */
  it('should open menu', function() {
    expect(browser.getCurrentUrl()).toEqual('http://localhost:8080/part/list');
    var cntrlEntities = element(by.id('nav-lnk-entities'));
    expect(cntrlEntities.isDisplayed()).toBeTruthy();
    var cntrlPartTypes = element(by.id('nav-lnk-parttypes'));
    expect(cntrlPartTypes.isDisplayed()).toBeFalsy();
    cntrlEntities.click();
    expect(cntrlPartTypes.isDisplayed()).toBeTruthy();
  });

});

