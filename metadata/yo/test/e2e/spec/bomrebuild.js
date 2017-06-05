// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('BOM reubuilding:', function() {

  var cntrlReindexTurbos, bttnStart, bttnClose, dlg;

  beforeAll(function() {
    cntrlReindexTurbos = element(by.tagName('input'));
    bttnStart = element(by.tiButton('Start'));
    bttnClose = element(by.buttonText('Close'));
    dlg = element(by.css('.modal-dialog'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/bom/rebuild/status');
    // Close a dialog with a result of a previous run if it is present.
    dlg.isDisplayed().then(function(displayed) {
      if (displayed) {
        bttnClose.click();
      }
    });
  });

  it('should has UI controls', function() {
    expect(cntrlReindexTurbos.isPresent()).toBeTruthy();
    expect(cntrlReindexTurbos.isEnabled()).toBeTruthy();
    expect(cntrlReindexTurbos.isSelected()).toBeTruthy();
    expect(bttnStart.isPresent()).toBeTruthy();
    expect(bttnStart.isEnabled()).toBeTruthy();
  });

  it('should rebuild BOM when button \'Start\' is pressed', function() {
    expect(dlg.isDisplayed()).toBeFalsy();
    bttnStart.click();
    expect(dlg.isDisplayed()).toBeTruthy();
  });

});
