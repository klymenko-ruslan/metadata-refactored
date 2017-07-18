// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Part details:', function() {

  var bttnCreateXRef, bttnWhereUsed, bttnInterchanges, bttnSalesNotes,
    lblStateActive, lblStateInactive, bttnReindex, bttnRebuildBOM, bttnReload,
    tabDetails, tabDimensions, tabTurboTypes, tabApplications, tabTurbos,
    tabNonStandard, tabAuditLog, tabPrices, tabAlsoBought, ;

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
    tabDetails = element(by.id('tab-details'));
    tabDimensions = element(by.id('tab-critical-dimensions'));
    tabTurboTypes = element(by.id('tab-turbo-types'));
    tabApplications = element(by.id('tab-applications'));
    tabTurbos = element(by.id('tab-turbos'));
    tabNonStandard = element(by.id('tab-nonstandard'));
    tabAuditLog = element(by.id('tab-audit-log'));
    tabPrices = element(by.id('tab-prices'));
    tabAlsoBought = element(by.id('tab-also-bought'));
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
    expect(tabDetails.isPresent()).toBeTruthy();
    expect(tabDetails.isDisplayed()).toBeTruthy();
    expect(tabDetails.isEnabled()).toBeTruthy();
    expect(tabDimensions.isPresent()).toBeTruthy();
    expect(tabDimensions.isDisplayed()).toBeTruthy();
    expect(tabDimensions.isEnabled()).toBeTruthy();
    expect(tabTurboTypes.isPresent()).toBeTruthy();
    expect(tabTurboTypes.isDisplayed()).toBeTruthy();
    expect(tabTurboTypes.isEnabled()).toBeTruthy();
    expect(tabApplications.isPresent()).toBeTruthy();
    expect(tabApplications.isDisplayed()).toBeFalsy();
    expect(tabTurbos.isPresent()).toBeTruthy();
    expect(tabTurbos.isDisplayed()).toBeFalsy();
    expect(tabNonStandard.isPresent()).toBeTruthy();
    expect(tabNonStandard.isDisplayed()).toBeFalsy();
    expect(tabAuditLog.isPresent()).toBeTruthy();
    expect(tabAuditLog.isDisplayed()).toBeTruthy();
    expect(tabAuditLog.isEnabled()).toBeTruthy();
    expect(tabPrices.isPresent()).toBeTruthy();
    expect(tabPrices.isDisplayed()).toBeTruthy();
    expect(tabPrices.isEnabled()).toBeTruthy();
    expect(tabAlsoBought.isPresent()).toBeTruthy();
    expect(tabAlsoBought.isDisplayed()).toBeTruthy();
    expect(tabAlsoBought.isEnabled()).toBeTruthy();
  });

  fit('has clickable tabs', function() {
    expect(tabDetails.getAttribute('aria-expanded')).toEqual('true');
    expect(tabDimensions.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurboTypes.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabApplications.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurbos.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabNonStandard.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAuditLog.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabPrices.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAlsoBought.getAttribute('aria-expanded')).not.toEqual('true');
    tabDimensions.click();
    expect(tabDetails.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabDimensions.getAttribute('aria-expanded')).toEqual('true');
    expect(tabTurboTypes.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabApplications.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurbos.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabNonStandard.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAuditLog.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabPrices.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAlsoBought.getAttribute('aria-expanded')).not.toEqual('true');
    tabTurboTypes.click();
    expect(tabDetails.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabDimensions.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurboTypes.getAttribute('aria-expanded')).toEqual('true');
    expect(tabApplications.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurbos.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabNonStandard.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAuditLog.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabPrices.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAlsoBought.getAttribute('aria-expanded')).not.toEqual('true');
    tabAuditLog.click();
    expect(tabDetails.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabDimensions.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurboTypes.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabApplications.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurbos.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabNonStandard.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAuditLog.getAttribute('aria-expanded')).toEqual('true');
    expect(tabPrices.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAlsoBought.getAttribute('aria-expanded')).not.toEqual('true');
    tabPrices.click();
    expect(tabDetails.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabDimensions.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurboTypes.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabApplications.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurbos.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabNonStandard.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAuditLog.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabPrices.getAttribute('aria-expanded')).toEqual('true');
    expect(tabAlsoBought.getAttribute('aria-expanded')).not.toEqual('true');
    tabAlsoBought.click();
    expect(tabDetails.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabDimensions.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurboTypes.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabApplications.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabTurbos.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabNonStandard.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAuditLog.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabPrices.getAttribute('aria-expanded')).not.toEqual('true');
    expect(tabAlsoBought.getAttribute('aria-expanded')).toEqual('true');
 });

});
