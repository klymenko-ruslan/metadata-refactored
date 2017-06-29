// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Applications & Turbos:', function() {

  var bttnDoit, tabHeaderTurbos, tabTurbos, rowsTurbos,
    tabHeaderApps, tabApps, rowsApps,
    pickedPartsPane, pickedTurbosRows, pickedAppsRows, bttnUnpickAllTurbos,
    bttnUnpickAllApps, fltrPartType;

  beforeAll(function() {
    bttnDoit = element(by.tiButton('Do it!'));
    var tabHeaders = element(by.id('atTabs')).all(by.tagName('li'));
    tabHeaderTurbos = tabHeaders.first().element(by.tagName('a'));
    tabTurbos = element(by.id('parts'));
    rowsTurbos = tabTurbos.all(by.repeater('$part in $data'));
    tabHeaderApps = tabHeaders.last().element(by.tagName('a'));
    tabApps = element(by.id('apps'));
    rowsApps = tabApps.all(by.repeater('rec in $data'));
    pickedPartsPane = element(by.id('pickedParts'));
    var pickedTurbosPane = element(by.id('pickedTurbos'));
    pickedTurbosRows = pickedTurbosPane.all(by.repeater('rec in $data'));
    var pickedAppsPane = element(by.id('pickedApps'));
    pickedAppsRows = pickedAppsPane.all(by.repeater('rec in $data'));
    bttnUnpickAllTurbos = element(by.id('unpickAllTurbos'));
    bttnUnpickAllApps = element(by.id('unpickAllApps'));
    fltrPartType = tabTurbos.element(by.id('fltrPartType'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/other/appsturbos');
  });

  it('shoul have an initial state', function() {
    expect(pickedPartsPane.isPresent()).toBeTruthy();
    expect(pickedPartsPane.isDisplayed()).toBeFalsy();
    expect(pickedTurbosRows.count()).toBe(0);
    expect(pickedAppsRows.count()).toBe(0);
    expect(bttnUnpickAllTurbos.isPresent()).toBeTruthy();
    expect(bttnUnpickAllTurbos.isDisplayed()).toBeFalsy();
    expect(bttnUnpickAllApps.isPresent()).toBeTruthy();
    expect(bttnUnpickAllApps.isDisplayed()).toBeFalsy();
    expect(bttnDoit.isPresent()).toBeTruthy();
    expect(bttnDoit.isDisplayed()).toBeTruthy();
    expect(bttnDoit.isEnabled()).toBeFalsy();
    expect(tabHeaderTurbos.isPresent()).toBeTruthy();
    expect(tabHeaderTurbos.isDisplayed()).toBeTruthy();
    expect(tabTurbos.isPresent()).toBeTruthy();
    expect(tabTurbos.isDisplayed()).toBeTruthy();
    expect(tabHeaderApps.isPresent()).toBeTruthy();
    expect(tabHeaderApps.isDisplayed()).toBeTruthy();
    expect(tabApps.isPresent()).toBeTruthy();
    expect(tabApps.isDisplayed()).toBeFalsy();
  });

  describe('Turbo Finder:', function() {

    var bttnClear, fltrPartNumber, fltrState, fltrManufacturer, fltrName;

    beforeAll(function() {
      bttnClear = tabTurbos.element(by.tiButton('Clear'));
      fltrPartNumber = tabTurbos.element(by.id('fltrPartNumber'));
      fltrState = tabTurbos.element(by.id('fltrState'));
      fltrManufacturer = tabTurbos.element(by.id('fltrManufacturer'));
      fltrName = tabTurbos.element(by.id('fltrName'));
    });

    it('should have an initial state', function() {
      expect(tabHeaderApps.isDisplayed()).toBeTruthy();
      expect(bttnClear.isPresent()).toBeTruthy();
      expect(bttnClear.isDisplayed()).toBeTruthy();
      expect(bttnClear.isEnabled()).toBeTruthy();
      expect(fltrPartNumber.isPresent()).toBeTruthy();
      expect(fltrPartNumber.isDisplayed()).toBeTruthy();
      expect(fltrPartNumber.isEnabled()).toBeTruthy();
      expect(fltrState.isPresent()).toBeTruthy();
      expect(fltrState.isDisplayed()).toBeTruthy();
      expect(fltrState.isEnabled()).toBeTruthy();
      expect(fltrManufacturer.isPresent()).toBeTruthy();
      expect(fltrManufacturer.isDisplayed()).toBeTruthy();
      expect(fltrManufacturer.isEnabled()).toBeTruthy();
      expect(fltrName.isPresent()).toBeTruthy();
      expect(fltrName.isDisplayed()).toBeTruthy();
      expect(fltrName.isEnabled()).toBeTruthy();
      expect(fltrPartType.isPresent()).toBeTruthy();
      expect(fltrPartType.isDisplayed()).toBeTruthy();
      expect(fltrPartType.isEnabled()).toBeTruthy();
      expect(rowsTurbos.count()).toBe(10);
    });

    describe('Filter:', function() {

      beforeEach(function() {
        bttnClear.click();
      });

      it('should clear all filter inputs', function() {
        fltrPartNumber.sendKeys('7-K');
        browser._selectDropdownbyNum(fltrState, 1);
        browser._selectDropdownbyNum(fltrManufacturer, 1);
        fltrName.sendKeys('Nissan');
        browser._selectDropdownbyNum(fltrPartType, 18);
        bttnClear.click();
        expect(fltrPartNumber.evaluate('fltrPart.partNumber')).toBeNull();
        expect(fltrState.evaluate('fltrPart.inactive')).toBeNull();
        expect(fltrManufacturer.evaluate('fltrPart.manufacturer')).toBeNull();
        expect(fltrName.evaluate('fltrPart.name')).toBeNull();
        expect(fltrPartType.evaluate('fltrPart.partType')).toBeNull();
      });

      describe('Part Number:', function() {

        it('should search by exact value', function() {
          fltrPartNumber.sendKeys('7-K-1132');
          expect(rowsTurbos.count()).toBe(1);
        });

        it('should search by partial value, case sensitive', function() {
          fltrPartNumber.sendKeys('32');
          expect(rowsTurbos.count()).toBe(3);
        });

        it('should search by partial value, case insensitive', function() {
          fltrPartNumber.sendKeys('7-k');
          expect(rowsTurbos.count()).toBe(2);
        });

      });

      describe('State:', function() {

        it('Active', function() {
          browser._selectDropdownbyNum(fltrState, 1);
          expect(rowsTurbos.count()).toBe(10);
        });

        it('Inactive', function() {
          browser._selectDropdownbyNum(fltrState, 2);
          expect(rowsTurbos.count()).toBe(2);
        });

      });

      describe('Manufacturer:', function() {

        it('Garret', function() {
          browser._selectDropdownbyNum(fltrManufacturer, 1);
          expect(rowsTurbos.count()).toBe(3);
        });

        it('Schwitzer', function() {
          browser._selectDropdownbyNum(fltrManufacturer, 4);
          expect(rowsTurbos.count()).toBe(1);
        });

      });

      describe('Name:', function() {

        it('should filter by exact value', function() {
          fltrName.sendKeys('Carbon Seal');
          expect(rowsTurbos.count()).toBe(2);
        });

        it('should filter by partial value, case-sensitive', function() {
          fltrName.sendKeys('Seal');
          expect(rowsTurbos.count()).toBe(2);
        });

        it('should filter by partial value, case-insensitive', function() {
          fltrName.sendKeys('seal');
          expect(rowsTurbos.count()).toBe(2);
        });

      });

      describe('Part Type:', function() {

        it('Actuator', function() {
          browser._selectDropdownbyNum(fltrPartType, 1);
          expect(rowsTurbos.count()).toBe(0);
        });

        it('Backplate', function() {
          browser._selectDropdownbyNum(fltrPartType, 2);
          expect(rowsTurbos.count()).toBe(3);
        });

        it('Turbo', function() {
          browser._selectDropdownbyNum(fltrPartType, 42);
          expect(rowsTurbos.count()).toBe(4);
        });

      });

    });

    it('should allow to pick/unpick turbos', function() {
      browser._selectDropdownbyNum(fltrPartType, 42); // Turbos
      expect(rowsTurbos.count()).toBe(4);
      rowsTurbos.get(0).element(by.tiButton('Pick')).click();
      rowsTurbos.get(1).element(by.tiButton('Pick')).click();
      rowsTurbos.get(2).element(by.tiButton('Pick')).click();
      rowsTurbos.get(3).element(by.tiButton('Pick')).click();
      expect(pickedPartsPane.isDisplayed()).toBeTruthy();
      expect(pickedTurbosRows.count()).toBe(4);
      expect(bttnUnpickAllTurbos.isDisplayed()).toBeTruthy();
      expect(bttnUnpickAllTurbos.isEnabled()).toBeTruthy();
      expect(pickedAppsRows.count()).toBe(0);
      expect(bttnUnpickAllApps.isDisplayed()).toBeFalsy();
      // Unpick a picked turbos in the first row.
      var bttnUnpick = pickedTurbosRows.first().element(by.tiButton('Unpick'));
      expect(bttnUnpick.isPresent()).toBeTruthy();
      expect(bttnUnpick.isDisplayed()).toBeTruthy();
      expect(bttnUnpick.isEnabled()).toBeTruthy();
      // Unpick the first row and make sure that it is disappeared
      // in the table.
      bttnUnpick.click();
      expect(pickedTurbosRows.count()).toBe(3);
      expect(pickedAppsRows.count()).toBe(0);
      // Unpick all.
      bttnUnpickAllTurbos.click();
      expect(pickedPartsPane.isDisplayed()).toBeFalsy();
      expect(pickedTurbosRows.count()).toBe(0);
      expect(pickedAppsRows.count()).toBe(0);
    });

  });


  describe('Application Finder', function() {

    var bttnClear, fltrApp, fltrYear, fltrMake, fltrModel,
      fltrEngine, fltrFuelType;

    beforeAll(function() {
      bttnClear = tabApps.element(by.tiButton('Clear'));
      fltrApp = tabApps.element(by.id('partApplication'));
      fltrYear = tabApps.element(by.id('fltrCmeyYear'));
      fltrMake = tabApps.element(by.id('fltrCmeyMake'));
      fltrModel = tabApps.element(by.id('fltrCmeyModel'));
      fltrEngine = tabApps.element(by.id('fltrCmeyEngine'));
      fltrFuelType = tabApps.element(by.id('fltrCmeyFueltype'));
    });

    beforeEach(function() {
      tabHeaderApps.click();
    });

    it('should have an initial state', function() {
      expect(tabTurbos.isDisplayed()).toBeFalsy();
      expect(tabApps.isDisplayed()).toBeTruthy();
      expect(bttnClear.isPresent()).toBeTruthy();
      expect(bttnClear.isDisplayed()).toBeTruthy();
      expect(bttnClear.isEnabled()).toBeTruthy();
      expect(fltrApp.isPresent()).toBeTruthy();
      expect(fltrApp.isDisplayed()).toBeTruthy();
      expect(fltrApp.isEnabled()).toBeTruthy();
      expect(fltrYear.isPresent()).toBeTruthy();
      expect(fltrYear.isDisplayed()).toBeTruthy();
      expect(fltrYear.isEnabled()).toBeTruthy();
      expect(fltrMake.isPresent()).toBeTruthy();
      expect(fltrMake.isDisplayed()).toBeTruthy();
      expect(fltrMake.isEnabled()).toBeTruthy();
      expect(fltrModel.isPresent()).toBeTruthy();
      expect(fltrModel.isDisplayed()).toBeTruthy();
      expect(fltrModel.isEnabled()).toBeTruthy();
      expect(fltrEngine.isPresent()).toBeTruthy();
      expect(fltrEngine.isDisplayed()).toBeTruthy();
      expect(fltrEngine.isEnabled()).toBeTruthy();
      expect(fltrFuelType.isPresent()).toBeTruthy();
      expect(fltrFuelType.isDisplayed()).toBeTruthy();
      expect(fltrFuelType.isEnabled()).toBeTruthy();
      expect(rowsApps.count()).toBe(8);
    });

    describe('Filter:', function() {

      beforeEach(function() {
        bttnClear.click();
      });

      it('should clear all filter inputs', function() {
        fltrApp.sendKeys('frontera');
        browser._selectDropdownbyNum(fltrYear, 1);
        browser._selectDropdownbyNum(fltrMake, 1);
        browser._selectDropdownbyNum(fltrModel, 1);
        browser._selectDropdownbyNum(fltrEngine, 1);
        browser._selectDropdownbyNum(fltrFuelType, 1);
        expect(rowsApps.count()).toBe(1);
        bttnClear.click();
        expect(rowsApps.count()).toBe(8);
        expect(fltrApp.evaluate('fltrCmey.cmey')).toBeNull();
        expect(fltrYear.evaluate('fltrCmey.year')).toBeNull();
        expect(fltrMake.evaluate('fltrCmey.make')).toBeNull();
        expect(fltrModel.evaluate('fltrCmey.model')).toBeNull();
        expect(fltrEngine.evaluate('fltrCmey.engine')).toBeNull();
        expect(fltrFuelType.evaluate('fltrCmey.fueltype')).toBeNull();
      });

      describe('Application:', function() {

        it('should search by exact value', function() {
          fltrApp.sendKeys('Volvo');
          expect(rowsApps.count()).toBe(4);
        });

        it('should search by partial value, case sensitive', function() {
          fltrApp.sendKeys('Vol');
          expect(rowsApps.count()).toBe(4);
        });

        it('should search by partial value, case insensitive', function() {
          fltrApp.sendKeys('vol');
          expect(rowsApps.count()).toBe(4);
        });

      });

      it('Year', function() {
        browser._selectDropdownbyNum(fltrYear, 1);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrYear);
        browser._selectDropdownbyNum(fltrYear, 2);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrYear);
        browser._selectDropdownbyNum(fltrYear, 3);
        expect(rowsApps.count()).toBe(1);
      });

      it('Make', function() {
        browser._selectDropdownbyNum(fltrMake, 1);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrMake);
        browser._selectDropdownbyNum(fltrMake, 2);
        expect(rowsApps.count()).toBe(4);
      });

      it('Model', function() {
        browser._selectDropdownbyNum(fltrModel, 1);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrModel);
        browser._selectDropdownbyNum(fltrModel, 2);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrModel);
        browser._selectDropdownbyNum(fltrModel, 3);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrModel);
        browser._selectDropdownbyNum(fltrModel, 4);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrModel);
        browser._selectDropdownbyNum(fltrModel, 5);
        expect(rowsApps.count()).toBe(1);
      });

      it('Engine', function() {
        browser._selectDropdownbyNum(fltrEngine, 1);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 2);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 3);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 4);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 5);
        expect(rowsApps.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 6);
        expect(rowsApps.count()).toBe(1);
      });

      it('Fuel Type', function() {
        browser._selectDropdownbyNum(fltrFuelType, 1);
        expect(rowsApps.count()).toBe(1);
      });

    });

    it('should allow to pick/unpick applications', function() {
      expect(rowsApps.count()).toBe(8);
      rowsApps.get(0).element(by.tiButton('Pick')).click();
      rowsApps.get(1).element(by.tiButton('Pick')).click();
      rowsApps.get(2).element(by.tiButton('Pick')).click();
      rowsApps.get(3).element(by.tiButton('Pick')).click();
      rowsApps.get(4).element(by.tiButton('Pick')).click();
      rowsApps.get(5).element(by.tiButton('Pick')).click();
      rowsApps.get(6).element(by.tiButton('Pick')).click();
      rowsApps.get(7).element(by.tiButton('Pick')).click();
      expect(pickedPartsPane.isDisplayed()).toBeTruthy();
      expect(pickedAppsRows.count()).toBe(8);
      expect(bttnUnpickAllApps.isDisplayed()).toBeTruthy();
      expect(bttnUnpickAllApps.isEnabled()).toBeTruthy();
      expect(pickedTurbosRows.count()).toBe(0);
      expect(bttnUnpickAllTurbos.isDisplayed()).toBeFalsy();
      // Unpick a picked app in the first row.
      var bttnUnpick = pickedAppsRows.first().element(by.tiButton('Unpick'));
      expect(bttnUnpick.isPresent()).toBeTruthy();
      expect(bttnUnpick.isDisplayed()).toBeTruthy();
      expect(bttnUnpick.isEnabled()).toBeTruthy();
      // Unpick the first row and make sure that it is disappeared
      // in the table.
      bttnUnpick.click();
      expect(pickedAppsRows.count()).toBe(7);
      expect(pickedTurbosRows.count()).toBe(0);
      // Unpick all.
      bttnUnpickAllApps.click();
      expect(pickedPartsPane.isDisplayed()).toBeFalsy();
      expect(pickedTurbosRows.count()).toBe(0);
      expect(pickedAppsRows.count()).toBe(0);
    });

  });

  it('should \'Do it!\'', function() {
    expect(bttnDoit.isEnabled()).toBeFalsy();
    browser._selectDropdownbyNum(fltrPartType, 42); // Turbos
    expect(rowsTurbos.count()).toBe(4);
    rowsTurbos.get(0).element(by.tiButton('Pick')).click();
    rowsTurbos.get(1).element(by.tiButton('Pick')).click();
    expect(bttnDoit.isEnabled()).toBeFalsy();
    tabHeaderApps.click();
    expect(bttnDoit.isEnabled()).toBeFalsy();
    rowsApps.get(0).element(by.tiButton('Pick')).click();
    rowsApps.get(1).element(by.tiButton('Pick')).click();
    expect(bttnDoit.isEnabled()).toBeTruthy();
    bttnDoit.click();
  });

});
