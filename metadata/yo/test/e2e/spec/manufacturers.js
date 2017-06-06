// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Manufacturers:', function() {

  var rows, firstRowTds, fltrName, fltrType, fltrNotExternal,
    bttnCreateManufacturer, bttnClear, bttnRemove, bttnModify, dlg;

  beforeAll(function() {
    rows = element.all(by.repeater('m in $data'));
    firstRowTds = rows.first().all(by.tagName('td'));
    var filterRow = element(by.className('ng-table-filters'));
    var ths = filterRow.all(by.tagName('th'));
    fltrName = ths.get(0).element(by.name('name'));
    fltrType = ths.get(1).element(by.name('typeId'));
    fltrNotExternal = ths.get(2).element(by.name('notExternal'));
    bttnClear = ths.get(3).element(by.tagName('button'));
    bttnCreateManufacturer = element(by.tiButton('Create Manufacturer'));
    bttnRemove = firstRowTds.last().element(by.tiButton('Remove'));
    bttnModify = firstRowTds.last().element(by.tiButton('Modify'));
    dlg = element(by.css('.modal-dialog'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/manufacturer/list');
    // Clear filter.
    fltrName.clear();
    browser._selectReset(fltrType);
    browser._selectReset(fltrNotExternal);
  });

  it('should has a list of manufacturers', function() {
    expect(rows.count()).toBe(11);
    expect(firstRowTds.count()).toBe(4);
    expect(bttnCreateManufacturer.isPresent()).toBeTruthy();
    expect(bttnCreateManufacturer.isDisplayed()).toBeTruthy();
    expect(bttnCreateManufacturer.isEnabled()).toBeTruthy();
  });

  it('should open a dialog window when button \'Create Manufcaturer\' ' +
    'is clicked',
    function() {
      expect(dlg.isPresent()).toBeFalsy();
      bttnCreateManufacturer.click();
      expect(dlg.isPresent()).toBeTruthy();
      expect(dlg.isDisplayed()).toBeTruthy();
    }
  );

  it('should display confirmation dialog on clicking button \'Remove\'',
    function() {
      expect(bttnRemove.isPresent()).toBeTruthy();
      expect(bttnRemove.isDisplayed()).toBeTruthy();
      expect(bttnRemove.isEnabled()).toBeTruthy();
      expect(dlg.isPresent()).toBeFalsy();
      bttnRemove.click();
      expect(dlg.isPresent()).toBeTruthy();
      expect(dlg.isDisplayed()).toBeTruthy();
    }
  );

  describe('Contrlols state:', function() {

    var bttnSave, bttnRevert, bttnCancel,
      cntrlName, cntrlType, cntrlNotExternal;

    beforeAll(function() {
      var lastTd = firstRowTds.last();
      bttnSave = lastTd.element(by.tiButton('Save'));
      bttnRevert = lastTd.element(by.tiButton('Revert'));
      bttnCancel = lastTd.element(by.tiButton('Cancel'));
      cntrlName = firstRowTds.get(0).element(by.tagName('input'));
      cntrlType = firstRowTds.get(1).element(by.tagName('select'));
      cntrlNotExternal = firstRowTds.get(2).element(by.tagName('input'));
    });

    it('should display r/o controls if not edited', function() {
      expect(bttnModify.isPresent()).toBeTruthy();
      expect(bttnModify.isDisplayed()).toBeTruthy();
      expect(bttnModify.isEnabled()).toBeTruthy();
      expect(bttnSave.isPresent()).toBeTruthy();
      expect(bttnSave.isDisplayed()).toBeFalsy();
      expect(bttnRevert.isPresent()).toBeTruthy();
      expect(bttnRevert.isDisplayed()).toBeFalsy();
      expect(bttnCancel.isPresent()).toBeTruthy();
      expect(bttnCancel.isDisplayed()).toBeFalsy();
      expect(cntrlName.isPresent()).toBeFalsy();
      expect(cntrlType.isPresent()).toBeFalsy();
      expect(cntrlNotExternal.isPresent()).toBeFalsy();
    });

    describe('Editing:', function() {

      beforeEach(function() {
        bttnModify.click();
      });

      it('should display input controls on clicking button \'Modify\'',
        function() {
          expect(bttnModify.isPresent()).toBeTruthy();
          expect(bttnModify.isDisplayed()).toBeFalsy();
          expect(bttnSave.isPresent()).toBeTruthy();
          expect(bttnSave.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
          expect(bttnRevert.isPresent()).toBeTruthy();
          expect(bttnRevert.isDisplayed()).toBeTruthy();
          expect(bttnRevert.isEnabled()).toBeFalsy();
          expect(bttnCancel.isPresent()).toBeTruthy();
          expect(bttnCancel.isDisplayed()).toBeTruthy();
          expect(bttnCancel.isEnabled()).toBeTruthy();
          expect(cntrlName.isPresent()).toBeTruthy();
          expect(cntrlName.isDisplayed()).toBeTruthy();
          expect(cntrlName.isEnabled()).toBeTruthy();
          expect(cntrlType.isPresent()).toBeTruthy();
          expect(cntrlType.isDisplayed()).toBeTruthy();
          expect(cntrlType.isEnabled()).toBeTruthy();
          expect(cntrlNotExternal.isPresent()).toBeTruthy();
          expect(cntrlNotExternal.isDisplayed()).toBeTruthy();
          expect(cntrlNotExternal.isEnabled()).toBeTruthy();
        }
      );

      it('cancel', function() {
        bttnCancel.click();
        expect(bttnModify.isPresent()).toBeTruthy();
        expect(bttnModify.isDisplayed()).toBeTruthy();
        expect(bttnModify.isEnabled()).toBeTruthy();
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeFalsy();
        expect(bttnRevert.isPresent()).toBeTruthy();
        expect(bttnRevert.isDisplayed()).toBeFalsy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeFalsy();
        expect(cntrlName.isPresent()).toBeFalsy();
        expect(cntrlType.isPresent()).toBeFalsy();
        expect(cntrlNotExternal.isPresent()).toBeFalsy();
      });

      it('revert', function() {
        expect(cntrlName.evaluate('manufacturer.name')).toBe('Garrett');
        cntrlName.sendKeys('foo');
        expect(bttnSave.isEnabled()).toBeTruthy();
        expect(bttnRevert.isEnabled()).toBeTruthy();
        bttnRevert.click();
        expect(cntrlName.evaluate('manufacturer.name')).toBe('Garrett');
      });

    });

  });

  describe('Filter:', function() {

    it('should have UI controls to filter', function() {
      expect(fltrName.isPresent()).toBeTruthy();
      expect(fltrName.isDisplayed()).toBeTruthy();
      expect(fltrName.isEnabled()).toBeTruthy();
      expect(fltrType.isPresent()).toBeTruthy();
      expect(fltrType.isDisplayed()).toBeTruthy();
      expect(fltrType.isEnabled()).toBeTruthy();
      expect(fltrNotExternal.isPresent()).toBeTruthy();
      expect(fltrNotExternal.isDisplayed()).toBeTruthy();
      expect(fltrNotExternal.isEnabled()).toBeTruthy();
      expect(bttnClear.isPresent()).toBeTruthy();
      expect(bttnClear.isDisplayed()).toBeTruthy();
      expect(bttnClear.isEnabled()).toBeTruthy();
    });

    it('button \'Clear\'', function() {
      fltrName.sendKeys('foo');
      browser._selectDropdownbyNum(fltrType, 1);
      browser._selectDropdownbyNum(fltrNotExternal, 1);
      expect(rows.count()).toBe(0);
      bttnClear.click();
      expect(rows.count()).toBe(11);
    });

    describe('Name:', function() {

      it('exact', function() {
        fltrName.sendKeys('KKK');
        expect(rows.count(1));
      });

      it('partly, case-sensitive', function() {
        fltrName.sendKeys('subi');
        expect(rows.count(1));
      });

      it('partly, case-insensitive', function() {
        fltrName.sendKeys('SUBI');
        expect(rows.count(1));
      });

    });

    describe('Type:', function() {

      it('any', function() {
        browser._selectReset(fltrType);
        expect(rows.count(11));
      });

      it('option \'turbo\'', function() {
        browser._selectDropdownbyNum(fltrType, 1);
        expect(rows.count(11));
      });

    });

    describe('Not External', function() {

      it('any', function() {
        browser._selectReset(fltrNotExternal);
        expect(rows.count(11));
      });

      it('yes', function() {
        browser._selectDropdownbyNum(fltrNotExternal, 1);
        expect(rows.count(11));
      });

      it('no', function() {
        browser._selectDropdownbyNum(fltrNotExternal, 2);
        expect(rows.count(1));
      });

    });

  });

});
