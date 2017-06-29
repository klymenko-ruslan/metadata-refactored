// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

var EC = protractor.ExpectedConditions;

describe('Authentication providers:', function() {

  var rows, bttnCreate, tdAction, tdAction, bttnModify, bttnRemove, bttnSave,
    bttnUndo, bttnCancel, dlg;

  beforeAll(function() {
    rows = element.all(by.repeater('rec in $data.recs'));
    bttnCreate = element(by.partialLinkText('Create'));
    tdAction = rows.first().all(by.tagName('td')).get(5);
    bttnModify = tdAction.element(by.tiButton('Modify'));
    bttnRemove = tdAction.element(by.tiButton('Remove'));
    bttnSave = tdAction.element(by.tiButton('Save'));
    bttnUndo = tdAction.element(by.tiButton('Undo'));
    bttnCancel = tdAction.element(by.tiButton('Cancel'));
    dlg = element(by.css('.modal'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/security/auth_providers');
  });

  it('should has button \'Create\'', function() {
    expect(bttnCreate.isPresent()).toBeTruthy();
    expect(bttnCreate.isDisplayed()).toBeTruthy();
    expect(bttnCreate.isEnabled()).toBeTruthy();
  });

  it('should open a form to create auth. provider', function () {
    bttnCreate.click();
    expect(browser.getCurrentUrl())
      .toBe('http://localhost:8080/security/auth_providers/create');
  });

  it('should has a list of providers', function() {
    expect(rows.isPresent()).toBeTruthy();
    expect(rows.count()).toBe(2);
  });

  it('should has edit buttons', function() {
    expect(tdAction.isPresent())
    expect(bttnModify.isPresent()).toBeTruthy();
    expect(bttnModify.isEnabled()).toBeTruthy();
    expect(bttnModify.isDisplayed()).toBeTruthy();
    expect(bttnRemove.isPresent()).toBeTruthy();
    expect(bttnRemove.isEnabled()).toBeTruthy();
    expect(bttnRemove.isDisplayed()).toBeTruthy();
    expect(bttnSave.isDisplayed()).toBeFalsy();
    expect(bttnUndo.isDisplayed()).toBeFalsy();
    expect(bttnCancel.isDisplayed()).toBeFalsy();
  });

  it('should display confirmation dialog on delete', function() {
    expect(dlg.isPresent()).toBeFalsy();
    bttnRemove.click();
    browser.wait(EC.visibilityOf(dlg), 5000, 'A confirmation dialog ' +
      'to delete an authentication provider has not been displayed.');
  });

  describe('Create:', function() {

    var bttnProvidersList, bttnSave, cntrlName, cntrlHost, cntrlPort;

    beforeAll(function() {
      bttnProvidersList = element(by.tiButton('Providers List'));
      bttnSave = element(by.tiButton('Save'));
      cntrlName = element(by.id('authp_name'));
      cntrlHost = element(by.id('authp_host'));
      cntrlPort = element(by.id('authp_port'));
    });

    beforeEach(function() {
      browser.get('http://localhost:8080/security/auth_providers/create');
    });

    it('should have buttons', function() {
      expect(bttnProvidersList.isPresent()).toBeTruthy();
      expect(bttnProvidersList.isEnabled()).toBeTruthy();
      expect(bttnSave.isPresent()).toBeTruthy();
      expect(bttnSave.isEnabled()).toBeFalsy();
    });

    it('should have input controls', function() {
      expect(cntrlName.isPresent()).toBeTruthy();
      expect(cntrlName.isDisplayed()).toBeTruthy();
      expect(cntrlName.isEnabled()).toBeTruthy();
      expect(cntrlHost.isPresent()).toBeTruthy();
      expect(cntrlHost.isDisplayed()).toBeTruthy();
      expect(cntrlHost.isEnabled()).toBeTruthy();
      expect(cntrlPort.isPresent()).toBeTruthy();
      expect(cntrlPort.isDisplayed()).toBeTruthy();
      expect(cntrlPort.isEnabled()).toBeTruthy();
    });

    it('should navigate to providers list', function() {
      bttnProvidersList.click();
      expect(browser.getCurrentUrl())
        .toBe('http://localhost:8080/security/auth_providers');
    });

    it('should enable button \'Save\' when the form is valid', function() {
      expect(bttnSave.isEnabled()).toBeFalsy();
      cntrlName.sendKeys('xxx');
      cntrlHost.sendKeys('yyy');
      expect(bttnSave.isEnabled()).toBeTruthy();
    });

    // TODO: create an auth provider
    // TODO: validators

  });

  describe('Edit:', function() {

    var cntrlName, cntrlHost, cntrlPort, cntrlDomain, cntrlProtocol;

    beforeAll(function() {
      var tds = rows.first().all(by.tagName('td'));
      cntrlName = tds.get(0).element(by.model('modifyingRow.name'));
      cntrlHost = tds.get(1).element(by.model('modifyingRow.host'));
      cntrlPort = tds.get(2).element(by.model('modifyingRow.port'));
      cntrlDomain = tds.get(3).element(by.model('modifyingRow.domain'));
      cntrlProtocol = tds.get(4).element(by.model('modifyingRow.protocol'));
    });

    beforeEach(function() {
      browser.getCurrentUrl();
      browser.get('http://localhost:8080/security/auth_providers');
    });

    it('should not allow editing until button \'Modify\' is pressed',
      function() {
        expect(cntrlName.isDisplayed()).toBeFalsy();
        expect(cntrlHost.isDisplayed()).toBeFalsy();
        expect(cntrlPort.isDisplayed()).toBeFalsy();
        expect(cntrlDomain.isDisplayed()).toBeFalsy();
        expect(cntrlProtocol.isDisplayed()).toBeFalsy();
      }
    );

    describe('Editing:', function() {

      beforeEach(function() {
        bttnModify.click();
      });

      it('should have buttons in proper states', function() {
        expect(bttnModify.isDisplayed()).toBeFalsy();
        expect(bttnRemove.isDisplayed()).toBeFalsy();
        expect(bttnSave.isDisplayed()).toBeTruthy();
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(bttnUndo.isDisplayed()).toBeTruthy();
        expect(bttnUndo.isEnabled()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeTruthy();
        expect(bttnCancel.isEnabled()).toBeTruthy();
      });

      it('should have input controls in proper states', function() {
        expect(cntrlName.isDisplayed()).toBeTruthy();
        expect(cntrlHost.isDisplayed()).toBeTruthy();
        expect(cntrlPort.isDisplayed()).toBeTruthy();
        expect(cntrlDomain.isDisplayed()).toBeTruthy();
        expect(cntrlProtocol.isDisplayed()).toBeTruthy();
      });

      it('should undo', function () {
        var origName  = cntrlName.evaluate('modifyingRow.name');
        var origHost = cntrlHost.evaluate('modifyingRow.host');
        var origPort = cntrlPort.evaluate('modifyingRow.port');
        var origDomain = cntrlDomain.evaluate('modifyingRow.domain');
        var origProtocol = cntrlProtocol.evaluate('modifyingRow.protocol');
        origName.clear('foo');
        origName.sendKeys('foo');
        origHost.clear();
        origHost.sendKeys('boo');
        origPort.clear();
        origPort.sendKeys('80');
        origDomain.clear();
        origDomain.sendKeys('ddd');
        bttnUndo.click();
        expect(cntrlName.evaluate('modifyingRow.name')).toEqual(origName);
        expect(cntrlHost.evaluate('modifyingRow.host')).toEqual(origHost);
        expect(cntrlPort.evaluate('modifyingRow.port')).toEqual(origPort);
        expect(cntrlDomain.evaluate('modifyingRow.domain')).toEqual(origDomain);
        // TODO: orig protocol
      });

      it('should cancel', function() {
        bttnCancel.click();
        expect(cntrlName.isDisplayed()).toBeFalsy();
        expect(cntrlHost.isDisplayed()).toBeFalsy();
        expect(cntrlPort.isDisplayed()).toBeFalsy();
        expect(cntrlDomain.isDisplayed()).toBeFalsy();
        expect(cntrlProtocol.isDisplayed()).toBeFalsy();
        expect(bttnModify.isPresent()).toBeTruthy();
        expect(bttnModify.isEnabled()).toBeTruthy();
        expect(bttnModify.isDisplayed()).toBeTruthy();
        expect(bttnRemove.isPresent()).toBeTruthy();
        expect(bttnRemove.isEnabled()).toBeTruthy();
        expect(bttnRemove.isDisplayed()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeFalsy();
        expect(bttnUndo.isDisplayed()).toBeFalsy();
        expect(bttnCancel.isDisplayed()).toBeFalsy();
      });

    });

  });

});
