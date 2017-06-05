// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('MAS90 Synchronization', function() {

  var bttnStart, bttnClose, dlg;

  beforeAll(function() {
    bttnStart = element(by.tiButton('Start'));
    bttnClose = element(by.buttonText('Close'));
    dlg = element(by.css('.modal-dialog'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/mas90/sync/status');
    // Close a dialog with a result of a previous run if it is present.
    dlg.isDisplayed().then(function(displayed) {
      if (displayed) {
        bttnClose.click();
      }
    });
  });

  it('should has UI controls', function() {
    expect(bttnStart.isPresent());
    expect(bttnStart.isEnabled());
    expect(bttnStart.isDisplayed());
  });

});
