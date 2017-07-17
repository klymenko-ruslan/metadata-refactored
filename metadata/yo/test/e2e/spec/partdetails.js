// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

fdescribe('Part details:', function() {

  var bttnCreateXRef, bttnWhereUsed, bttnInterchanges, bttnSalesNotes,
    lblStateActive, lblStateInactive, bttnReindex, bttnRebuildBOM, bttnReload;

  beforeAll(function() {
    bttnCreateXRef = element(by.tiButton('Create X Ref'));
    bttnWhereUsed = element(by.partialLinkText('Where used'));
    bttnInterchanges = element(by.partialLinkText('Interchanges'));
    bttnSalesNotes = element(by.partialLinkText('Sales Notes'));
    lblStateActive = $('.nav-pills').$$('li').last().$$('strong').first();
    lblStateInactive = $('.nav-pills').$$('li').last().$$('strong').last();
    bttnReindex = element(by.tiButton('Reindex'));
    bttnRebuildBOM = element(by.tiButton('Rebuild BOM'));
    bttnReload = element(by.tiButton('Reload'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/part/64956');
  });

  it('should have an initial state', function() {
    expect(bttnCreateXRef.isPresent()).toBeTruthy();
    expect(bttnCreateXRef.isDisplayed()).toBeTruthy();
    expect(bttnCreateXRef.isEnabled()).toBeTruthy();
    expect(bttnWhereUsed.isPresent()).toBeTruthy();
    expect(bttnWhereUsed.isDisplayed()).toBeTruthy();
    expect(bttnWhereUsed.isEnabled()).toBeTruthy();
    expect(bttnInterchanges.isPresent()).toBeTruthy();
    expect(bttnInterchanges.isDisplayed()).toBeTruthy();
    expect(bttnInterchanges.isEnabled()).toBeTruthy();
    expect(bttnSalesNotes.isPresent()).toBeTruthy();
    expect(bttnSalesNotes.isDisplayed()).toBeTruthy();
    expect(bttnSalesNotes.isEnabled()).toBeTruthy();
    expect(lblStateActive.isPresent()).toBeTruthy();
    expect(lblStateActive.isDisplayed()).toBeTruthy();
    expect(lblStateInactive.isPresent()).toBeTruthy();
    expect(lblStateInactive.isDisplayed()).toBeFalsy();
    expect(bttnReindex.isPresent()).toBeTruthy();
    expect(bttnReindex.isDisplayed()).toBeTruthy();
    expect(bttnReindex.isEnabled()).toBeTruthy();
    expect(bttnRebuildBOM.isPresent()).toBeTruthy();
    expect(bttnRebuildBOM.isDisplayed()).toBeTruthy();
    expect(bttnRebuildBOM.isEnabled()).toBeTruthy();
    expect(bttnReload.isPresent()).toBeTruthy();
    expect(bttnReload.isDisplayed()).toBeTruthy();
    expect(bttnReload.isEnabled()).toBeTruthy();
  });

});
