// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Indexing of entities:', function() {

  var cntrlReindexTurbos, bttnStart, bttnClose, dlg, cntrlParts,
    cntrlApplications, cntrlSalesNotes, cntrlChangelogSources,
    cntrlReCreateIndex;

  beforeAll(function() {
    bttnStart = element(by.tiButton('Start'));
    bttnClose = element(by.buttonText('Close'));
    dlg = element(by.css('.modal-dialog'));
    cntrlParts = element(by.model('toIndex.parts'));
    cntrlApplications = element(by.model('toIndex.applications'));
    cntrlSalesNotes = element(by.model('toIndex.salesNotes'));
    cntrlChangelogSources = element(by.model('toIndex.changelogSources'));
    cntrlReCreateIndex = element(by.model('toIndex.recreateIndex'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/search/indexing/status');
    // Close a dialog with a result of a previous run if it is present.
    dlg.isDisplayed().then(function(displayed) {
      if (displayed) {
        bttnClose.click();
      }
    });
  });

  it('should has UI controls', function() {
    expect(cntrlParts.isPresent()).toBeTruthy();
    expect(cntrlParts.isDisplayed()).toBeTruthy();
    expect(cntrlParts.isEnabled()).toBeFalsy();
    expect(cntrlApplications.isPresent()).toBeTruthy();
    expect(cntrlApplications.isDisplayed()).toBeTruthy();
    expect(cntrlApplications.isEnabled()).toBeFalsy();
    expect(cntrlSalesNotes.isPresent()).toBeTruthy();
    expect(cntrlSalesNotes.isDisplayed()).toBeTruthy();
    expect(cntrlSalesNotes.isEnabled()).toBeFalsy();
    expect(cntrlChangelogSources.isPresent()).toBeTruthy();
    expect(cntrlChangelogSources.isDisplayed()).toBeTruthy();
    expect(cntrlChangelogSources.isEnabled()).toBeFalsy();
    expect(cntrlReCreateIndex.isPresent()).toBeTruthy();
    expect(cntrlReCreateIndex.isDisplayed()).toBeTruthy();
    expect(cntrlReCreateIndex.isEnabled()).toBeTruthy();
    expect(cntrlReCreateIndex.isSelected()).toBeTruthy();
    expect(bttnStart.isPresent()).toBeTruthy();
    expect(bttnStart.isDisplayed()).toBeTruthy();
    expect(bttnStart.isEnabled()).toBeTruthy();
  });

  it('should allow enable checkboxes for specific index(es) when checkbox ' +
    '\(Re)Create the index before update\' is unchecked',
    function() {
      cntrlReCreateIndex.click().then(function() {
        expect(cntrlParts.isEnabled()).toBeTruthy();
        expect(cntrlApplications.isEnabled()).toBeTruthy();
        expect(cntrlSalesNotes.isEnabled()).toBeTruthy();
        expect(cntrlChangelogSources.isEnabled()).toBeTruthy();
      });
    }
  );

  it('should rebuild BOM when button \'Start\' is pressed', function() {
    expect(dlg.isDisplayed()).toBeFalsy();
    bttnStart.click();
    expect(dlg.isDisplayed()).toBeTruthy();
  });

});
