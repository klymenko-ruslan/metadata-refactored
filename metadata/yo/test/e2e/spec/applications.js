// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Applications:', function() {

  var bttnModelEngineYear, bttnMake, bttnModel, bttnEngine, bttnFuelType;

  beforeAll(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/application/carmodelengineyear/list');
    bttnModelEngineYear = element(by.id('bttn-subnav-modelengineyear'));
    bttnMake = element(by.id('bttn-subnav-make'));
    bttnModel = element(by.id('bttn-subnave-model'));
    bttnEngine = element(by.id('bttn-subnav-engine'));
    bttnFuelType = element(by.id('bttn-subnav-fueltype'));
  });

  it('should have an initial state', function() {
    expect(bttnModelEngineYear.isPresent()).toBeTruthy();
    expect(bttnModelEngineYear.isDisplayed()).toBeTruthy();
    //expect(bttnModelEngineYear.isEnabled()).toBeFalsy();
    expect(bttnMake.isPresent()).toBeTruthy();
    expect(bttnMake.isDisplayed()).toBeTruthy();
    expect(bttnMake.isEnabled()).toBeTruthy();
    expect(bttnModel.isPresent()).toBeTruthy();
    expect(bttnModel.isDisplayed()).toBeTruthy();
    expect(bttnModel.isEnabled()).toBeTruthy();
    expect(bttnEngine.isPresent()).toBeTruthy();
    expect(bttnEngine.isDisplayed()).toBeTruthy();
    expect(bttnEngine.isEnabled()).toBeTruthy();
    expect(bttnFuelType.isPresent()).toBeTruthy();
    expect(bttnFuelType.isDisplayed()).toBeTruthy();
    expect(bttnFuelType.isEnabled()).toBeTruthy();
  });

  describe('Model Engine Year:', function() {

    var bttnCreate, rows, bttnClear, fltrApp, fltrYear, fltrMake, fltrModel,
      fltrEngine, fltrFuelType;

    beforeAll(function() {
      bttnCreate = element(by.partialLinkText('Create Model Engine Year'));
      bttnClear = element(by.tiButton('Clear'));
      fltrApp = element(by.id('partApplication'));
      fltrYear = element(by.id('fltrCmeyYear'));
      fltrMake = element(by.id('fltrCmeyMake'));
      fltrModel = element(by.id('fltrCmeyModel'));
      fltrEngine = element(by.id('fltrCmeyEngine'));
      fltrFuelType = element(by.id('fltrCmeyFueltype'));
      rows = element.all(by.repeater('rec in $data'));
    });

    beforeEach(function() {
      browser.get('http://localhost:8080/application/carmodelengineyear/list');
    });

    it('should have an initial state', function() {
      expect(bttnCreate.isPresent()).toBeTruthy();
      expect(bttnCreate.isDisplayed()).toBeTruthy();
      expect(bttnCreate.isEnabled()).toBeTruthy();
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
        expect(rows.count()).toBe(1);
        bttnClear.click();
        expect(rows.count()).toBe(8);
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
          expect(rows.count()).toBe(4);
        });

        it('should search by partial value, case sensitive', function() {
          fltrApp.sendKeys('Vol');
          expect(rows.count()).toBe(4);
        });

        it('should search by partial value, case insensitive', function() {
          fltrApp.sendKeys('vol');
          expect(rows.count()).toBe(4);
        });

      });

      it('Year', function() {
        browser._selectDropdownbyNum(fltrYear, 1);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrYear);
        browser._selectDropdownbyNum(fltrYear, 2);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrYear);
        browser._selectDropdownbyNum(fltrYear, 3);
        expect(rows.count()).toBe(1);
      });

      it('Make', function() {
        browser._selectDropdownbyNum(fltrMake, 1);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrMake);
        browser._selectDropdownbyNum(fltrMake, 2);
        expect(rows.count()).toBe(4);
      });

      it('Model', function() {
        browser._selectDropdownbyNum(fltrModel, 1);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrModel);
        browser._selectDropdownbyNum(fltrModel, 2);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrModel);
        browser._selectDropdownbyNum(fltrModel, 3);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrModel);
        browser._selectDropdownbyNum(fltrModel, 4);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrModel);
        browser._selectDropdownbyNum(fltrModel, 5);
        expect(rows.count()).toBe(1);
      });

      it('Engine', function() {
        browser._selectDropdownbyNum(fltrEngine, 1);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 2);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 3);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 4);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 5);
        expect(rows.count()).toBe(1);
        browser._selectReset(fltrEngine);
        browser._selectDropdownbyNum(fltrEngine, 6);
        expect(rows.count()).toBe(1);
      });

      it('Fuel Type', function() {
        browser._selectDropdownbyNum(fltrFuelType, 1);
        expect(rows.count()).toBe(1);
      });

    });

    describe('Table:', function() {

      var bttnView, bttnEdit, bttnRemove;

      beforeAll(function() {
        var firstRow = rows.first();
        bttnView = firstRow.element(by.partialLinkText('View'));
        bttnEdit = firstRow.element(by.partialLinkText('Edit'));
        bttnRemove = firstRow.element(by.tiButton('Remove'));
      });

      it('should have an initial state', function() {
        expect(rows.count()).toBe(8);
        expect(bttnView.isPresent()).toBeTruthy();
        expect(bttnView.isDisplayed()).toBeTruthy();
        expect(bttnView.isEnabled()).toBeTruthy();
        expect(bttnEdit.isPresent()).toBeTruthy();
        expect(bttnEdit.isDisplayed()).toBeTruthy();
        expect(bttnEdit.isEnabled()).toBeTruthy();
        expect(bttnRemove.isPresent()).toBeTruthy();
        expect(bttnRemove.isDisplayed()).toBeTruthy();
        expect(bttnRemove.isEnabled()).toBeTruthy();
      });

      it('should open details view when button \'View\' is clicked',
        function() {
          bttnView.click();
          expect(browser.getCurrentUrl())
            .toBe('http://localhost:8080/application/carmodelengineyear/9');
        }
      );

      it('should display a confirmation dialog when button \'Remove\' ' +
        'is clicked',
        function() {
          var dlg = element(by.css('.modal-dialog'));
          expect(dlg.isPresent()).toBeFalsy();
          bttnRemove.click();
          expect(dlg.isDisplayed()).toBeTruthy();
        }
      );

      it('should open an edit form when button \'Edit\' is clicked',
        function() {
          bttnEdit.click();
          expect(browser.getCurrentUrl())
            .toBe('http://localhost:8080/application/carmodelengineyear' +
              '/9/form');
        }
      );

      describe('View:', function() {

        var bttnEditModelEngineYear;

        beforeAll(function() {
          bttnEditModelEngineYear = element(
            by.id('bttn-edit-model-engine-year'));
        });

        beforeEach(function() {
          browser
            .get('http://localhost:8080/application/carmodelengineyear/9');
        });

        it('it should have an initial state', function() {
          expect(bttnModelEngineYear.isPresent()).toBeTruthy();
          expect(bttnModelEngineYear.isDisplayed()).toBeTruthy();
          expect(bttnModelEngineYear.isEnabled()).toBeTruthy();
          expect(bttnMake.isPresent()).toBeTruthy();
          expect(bttnMake.isDisplayed()).toBeTruthy();
          expect(bttnMake.isEnabled()).toBeTruthy();
          expect(bttnModel.isPresent()).toBeTruthy();
          expect(bttnModel.isDisplayed()).toBeTruthy();
          expect(bttnModel.isEnabled()).toBeTruthy();
          expect(bttnEngine.isPresent()).toBeTruthy();
          expect(bttnEngine.isDisplayed()).toBeTruthy();
          expect(bttnEngine.isEnabled()).toBeTruthy();
          expect(bttnFuelType.isPresent()).toBeTruthy();
          expect(bttnFuelType.isDisplayed()).toBeTruthy();
          expect(bttnFuelType.isEnabled()).toBeTruthy();
          expect(bttnEditModelEngineYear.isPresent()).toBeTruthy();
          expect(bttnEditModelEngineYear.isDisplayed()).toBeTruthy();
          expect(bttnEditModelEngineYear.isEnabled()).toBeTruthy();
        });

        it('should open an edit form when button \'Edit Model Engine Year\' ' +
          'is clicked',
          function() {
            bttnEditModelEngineYear.click();
            expect(browser.getCurrentUrl()).toBe('http://localhost:8080/' +
              'application/carmodelengineyear/9/form');
          }
        );

      });

      describe('Edit:', function() {

        var bttnViewModelEngineYear, bttnSave, bttnRevert;

        beforeAll(function() {
          bttnViewModelEngineYear = element(
            by.id('bttn-subnav-cmey-viewmodelengineyear'));
          bttnSave = element(by.id('bttn-subnav-cmey-save'));
          bttnRevert = element(by.id('bttn-subnav-cmey-revert'));
        });

        beforeEach(function() {
          browser.get('http://localhost:8080/application/' +
            'carmodelengineyear/9/form');
        });

        it('should have an initial state', function() {
          expect(bttnViewModelEngineYear.isPresent()).toBeTruthy();
          expect(bttnViewModelEngineYear.isDisplayed()).toBeTruthy();
          expect(bttnViewModelEngineYear.isEnabled()).toBeTruthy();
          expect(bttnSave.isPresent()).toBeTruthy();
          expect(bttnSave.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
          expect(bttnRevert.isPresent()).toBeTruthy();
          expect(bttnRevert.isDisplayed()).toBeTruthy();
          expect(bttnRevert.isEnabled()).toBeFalsy();
        });

      });

    });

  });

  describe('Make:', function() {

    beforeAll(function() {
    });

    beforeEach(function() {
    });

    it('', function() {
    });

  });

  describe('Model:', function() {

    beforeAll(function() {
    });

    beforeEach(function() {
    });

    it('', function() {
    });

  });

  describe('Engine:', function() {

    beforeAll(function() {
    });

    beforeEach(function() {
    });

    it('', function() {
    });

  });

  describe('Fuel Type:', function() {

    beforeAll(function() {
    });

    beforeEach(function() {
    });

    it('', function() {
    });

  });

});
