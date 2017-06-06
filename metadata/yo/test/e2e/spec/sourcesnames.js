// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Sources Names:', function() {

  var rows, firstRowTds, bttnModify, bttnRemove, bttnSave, bttnRevert,
    bttnCancel, cntrlName, dlg;

  beforeAll(function() {
    rows = element.all(by.repeater('rec in $data'));
    firstRowTds = rows.first().all(by.tagName('td'));
    var tdActions = firstRowTds.last();
    bttnModify = tdActions.element(by.tiButton('Modify'));
    bttnRemove = tdActions.element(by.tiButton('Remove'));
    bttnSave = tdActions.element(by.tiButton('Save'));
    bttnRevert = tdActions.element(by.tiButton('Revert'));
    bttnCancel = tdActions.element(by.tiButton('Cancel'));
    cntrlName = firstRowTds.first().element(by.tagName('input'));
    dlg = element(by.css('.modal-dialog'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/changelog/source/name/list');
  });

  it('should have a list of source names', function() {
    expect(rows.isPresent()).toBeTruthy();
    expect(rows.count()).toBe(19);
  });

  describe('Add:', function() {

    var cntrlNewName, bttnAdd;

    beforeAll(function() {
      cntrlNewName = element(by.id('new-name'));
      bttnAdd = element(by.tiButton('Add'));
    });

    it('initial state', function() {
      expect(cntrlNewName.isPresent()).toBeTruthy();
      expect(cntrlNewName.isDisplayed()).toBeTruthy();
      expect(cntrlNewName.isEnabled()).toBeTruthy();
      expect(bttnAdd.isPresent()).toBeTruthy();
      expect(bttnAdd.isDisplayed()).toBeTruthy();
      expect(bttnAdd.isEnabled()).toBeFalsy();
    });

    describe('should not add already existing items:', function() {

      beforeEach(function() {
        cntrlNewName.clear();
      });

      it('case sensitive', function() {
        cntrlNewName.sendKeys('Jrone');
        expect(bttnAdd.isEnabled()).toBeFalsy();
      });

      it('case insensitive', function() {
        cntrlNewName.sendKeys('jrone');
        expect(bttnAdd.isEnabled()).toBeFalsy();
      });

    });

    it('should allow to add a new unique name', function() {
      cntrlNewName.clear();
      cntrlNewName.sendKeys('foo');
      expect(bttnAdd.isEnabled()).toBeTruthy();
    });

  });

  describe('Edit:', function() {

    it('initial state', function() {
      expect(bttnModify.isPresent()).toBeTruthy();
      expect(bttnModify.isDisplayed()).toBeTruthy();
      expect(bttnModify.isEnabled()).toBeTruthy();
      expect(bttnRemove.isPresent()).toBeTruthy();
      expect(bttnRemove.isDisplayed()).toBeTruthy();
      expect(bttnRemove.isEnabled()).toBeTruthy();
      expect(bttnSave.isPresent()).toBeTruthy();
      expect(bttnSave.isDisplayed()).toBeFalsy();
      expect(bttnRevert.isPresent()).toBeTruthy();
      expect(bttnRevert.isDisplayed()).toBeFalsy();
      expect(bttnCancel.isPresent()).toBeTruthy();
      expect(bttnCancel.isDisplayed()).toBeFalsy();
    });

    it('should display a confirmation dialog on clicking button \'Remove\'',
      function() {
        expect(dlg.isPresent()).toBeFalsy();
        bttnRemove.click();
        expect(dlg.isPresent()).toBeTruthy();
        expect(dlg.isDisplayed()).toBeTruthy();
      }
    );

    describe('Modify:', function() {

      beforeEach(function() {
        bttnModify.click();
      });

      it('should change state of buttons', function() {
        expect(bttnModify.isPresent()).toBeTruthy();
        expect(bttnModify.isDisplayed()).toBeFalsy();
        expect(bttnRemove.isPresent()).toBeTruthy();
        expect(bttnRemove.isDisplayed()).toBeFalsy();
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeTruthy();
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(bttnRevert.isPresent()).toBeTruthy();
        expect(bttnRevert.isDisplayed()).toBeTruthy();
        expect(bttnRevert.isEnabled()).toBeFalsy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeTruthy();
        expect(bttnCancel.isEnabled()).toBeTruthy();
      });

      it('cancel', function() {
        bttnCancel.click();
        expect(bttnModify.isPresent()).toBeTruthy();
        expect(bttnModify.isDisplayed()).toBeTruthy();
        expect(bttnModify.isEnabled()).toBeTruthy();
        expect(bttnRemove.isPresent()).toBeTruthy();
        expect(bttnRemove.isDisplayed()).toBeTruthy();
        expect(bttnRemove.isEnabled()).toBeTruthy();
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeFalsy();
        expect(bttnRevert.isPresent()).toBeTruthy();
        expect(bttnRevert.isDisplayed()).toBeFalsy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeFalsy();
      });

      it('revert', function() {
        expect(cntrlName.evaluate('sourceName.name')).toBe('Buyautoparts.com');
        cntrlName.sendKeys('foo');
        expect(bttnSave.isEnabled()).toBeTruthy();
        expect(bttnRevert.isEnabled()).toBeTruthy();
        bttnRevert.click();
        expect(cntrlName.evaluate('sourceName.name')).toBe('Buyautoparts.com');
      });

    });

  });


});
