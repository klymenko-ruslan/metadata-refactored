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

    var bttnCreate, rows, bttnClear, fltrMake;

    beforeAll(function() {
      bttnCreate = element(by.partialLinkText('Create Make'));
      bttnClear = element(by.tiButton('Clear'));
      fltrMake = element(by.id('carmake'));
      rows = element.all(by.repeater('rec in $data'));
    });

    beforeEach(function() {
      browser.get('http://localhost:8080/application/carmake/list');
    });

    it('should have an initial state', function() {
      expect(bttnCreate.isPresent()).toBeTruthy();
      expect(bttnCreate.isDisplayed()).toBeTruthy();
      expect(bttnCreate.isEnabled()).toBeTruthy();
      expect(bttnClear.isPresent()).toBeTruthy();
      expect(bttnClear.isDisplayed()).toBeTruthy();
      expect(bttnClear.isEnabled()).toBeTruthy();
      expect(fltrMake.isPresent()).toBeTruthy();
      expect(fltrMake.isDisplayed()).toBeTruthy();
      expect(fltrMake.isEnabled()).toBeTruthy();
    });

    it('should open form to create a new \'Make\' when button ' +
      '\'Create Make\' is clicked',
      function() {
        bttnCreate.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carmake/form');
      }
    );

    describe('Create:', function() {

      var bttnSave, inptName;

      beforeAll(function() {
        bttnSave = element(by.tiButton('Save'));
        inptName = element(by.id('carmake_name'));
      });

      beforeEach(function() {
        browser.get('http://localhost:8080/application/carmake/form');
      });

      it('should have an initial state', function() {
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeTruthy();
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(inptName.isPresent()).toBeTruthy();
        expect(inptName.isDisplayed()).toBeTruthy();
        expect(inptName.isEnabled()).toBeTruthy();
      });

      it('should create a new Car Make', function() {
        inptName.sendKeys('foo');
        expect(bttnSave.isEnabled()).toBeTruthy();
        bttnSave.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carmake/list');
        // Find the just created Car Make and delete it.
        //
        // Because a datasource for the table is ElasticSearch
        // it is possible that deletion of a Car Make is not
        // reflected immediately (Elasticsearch is a near real time
        // search platform).
        // So we do the check by waiting of the expectation during
        // some time period.
        browser.wait(function() {
          bttnClear.click();
          fltrMake.sendKeys('foo');
          return rows.count().then(
            function(rowCount) {
              return rowCount === 1;
            }
          );
        }, 3000, 'A Car Make \'foo\' has not been created.');
        var firstRow = rows.first();
        var bttnRemove = firstRow.element(by.tiButton('Remove'));
        bttnRemove.click();
        // A confirmation dialog should be displayed.
        var dlg = element(by.css('.modal-dialog'));
        expect(dlg.isDisplayed()).toBeTruthy();
        var bttnYes = dlg.element(by.partialButtonText('Yes'));
        bttnYes.click();
        // Make sure that Car Make has really been removed.
        expect(dlg.isPresent()).toBeFalsy();
        browser.wait(function() {
          bttnClear.click();
          fltrMake.sendKeys('foo');
          return rows.count().then(
            function(rowCount) {
              return rowCount === 0;
            }
          );
        }, 3000, 'The just created Car Make \'foo\' was not deleted.');
      });

      describe('Validation:', function() {

        var errorArea, errRequired, errTooLong, errNotUnique;

        beforeAll(function() {
          errorArea = element(by.name('carmakeForm'))
            .element(by.css('.alert'));
          var errs = errorArea.all(by.tagName('span'));
          errRequired = errs.get(1);
          errTooLong = errs.get(2);
          errNotUnique = errs.get(3);
        });

        it('should not display any error before modification', function() {
          expect(errorArea.isPresent()).toBeTruthy();
          expect(errorArea.isDisplayed()).toBeFalsy();
          expect(errRequired.isPresent()).toBeTruthy();
          expect(errTooLong.isPresent()).toBeTruthy();
          expect(errNotUnique.isPresent()).toBeTruthy();
        });

        it('should check constraint \'required\'', function() {
          inptName.sendKeys('foo');  // make dirty a form to start validation
          inptName.clear();
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeTruthy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeFalsy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

        xit('should check constraint \'maxlength\'', function() {
          // This test will fail beacuse 'input' does not allow
          // to exceed 'maxlength'.
          var longString = (new Array(100)).join('x');
          inptName.sendKeys(longString);
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeFalsy();
          expect(errTooLong.isDisplayed()).toBeTruthy();
          expect(errNotUnique.isDisplayed()).toBeFalsy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

        it('should check constraint \'unique\'', function() {
          inptName.clear();
          inptName.sendKeys('ARO');
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeFalsy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

      });

    });

    describe('Filter:', function() {

      beforeEach(function() {
        bttnClear.click();
      });

      it('should clear all filter inputs', function() {
        fltrMake.sendKeys('Alfa Romeo');
        expect(rows.count()).toBe(1);
        bttnClear.click();
        expect(rows.count()).toBe(10);
        expect(fltrMake.evaluate('search.carmake')).toBe('');
      });

      describe('Carmake:', function() {

        it('should search by exact value', function() {
          fltrMake.sendKeys('Alfa Rome');
          expect(rows.count()).toBe(1);
        });

        it('should search by partial value, case sensitive', function() {
          fltrMake.sendKeys('tos');
          expect(rows.count()).toBe(2);
        });

        it('should search by partial value, case insensitive', function() {
          fltrMake.sendKeys('TOS');
          expect(rows.count()).toBe(2);
        });

      });

    });

    describe('Table:', function() {

      var firstRow, firstCell, bttnModify, bttnRemove, bttnSave, bttnCancel, inptName;

      beforeAll(function() {
        firstRow = rows.first();
        firstCell = firstRow.all(by.tagName('td')).first()
          .all(by.tagName('span')).first();
        bttnModify = firstRow.element(by.tiButton('Modify'));
        bttnRemove = firstRow.element(by.tiButton('Remove'));
        bttnSave = firstRow.element(by.tiButton('Save'));
        bttnCancel = firstRow.element(by.tiButton('Cancel'));
        inptName = firstRow.element(by.name('carmake_name'));
      });

      it('should have an initial state', function() {
        expect(rows.count()).toBe(10);
        expect(bttnModify.isPresent()).toBeTruthy();
        expect(bttnModify.isDisplayed()).toBeTruthy();
        expect(bttnModify.isEnabled()).toBeTruthy();
        expect(bttnRemove.isPresent()).toBeTruthy();
        expect(bttnRemove.isDisplayed()).toBeTruthy();
        expect(bttnRemove.isEnabled()).toBeTruthy();
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeFalsy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeFalsy();
        expect(inptName.isPresent()).toBeTruthy();
        expect(inptName.isDisplayed()).toBeFalsy();
      });

      it('should display a confirmation dialog when button \'Remove\' ' +
        'is clicked',
        function() {
          var dlg = element(by.css('.modal-dialog'));
          expect(dlg.isPresent()).toBeFalsy();
          bttnRemove.click();
          expect(dlg.isDisplayed()).toBeTruthy();
        }
      );

      describe('Modify:', function() {

        beforeEach(function() {
          bttnModify.click();
        });

        it('should have an initial state', function() {
          expect(bttnModify.isPresent()).toBeTruthy();
          expect(bttnModify.isDisplayed()).toBeFalsy();
          expect(bttnRemove.isPresent()).toBeTruthy();
          expect(bttnRemove.isDisplayed()).toBeFalsy();
          expect(bttnSave.isPresent()).toBeTruthy();
          expect(bttnSave.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
          expect(bttnCancel.isPresent()).toBeTruthy();
          expect(bttnCancel.isDisplayed()).toBeTruthy();
          expect(bttnCancel.isEnabled()).toBeTruthy();
          expect(inptName.isPresent()).toBeTruthy();
          expect(inptName.isDisplayed()).toBeTruthy();
          expect(inptName.evaluate('modifyValues[rec._source.id]'))
            .toBe('Agrale');
        });

        it('should undo changes when button \'Cancel\' is clicked',
          function() {
            inptName.sendKeys('Foo');
            expect(inptName.evaluate('modifyValues[rec._source.id]'))
              .toBe('AgraleFoo');
            bttnCancel.click();
            expect(firstCell.getText()).toBe('Agrale');
            expect(bttnModify.isPresent()).toBeTruthy();
            expect(bttnModify.isDisplayed()).toBeTruthy();
            expect(bttnModify.isEnabled()).toBeTruthy();
            expect(bttnRemove.isPresent()).toBeTruthy();
            expect(bttnRemove.isDisplayed()).toBeTruthy();
            expect(bttnRemove.isEnabled()).toBeTruthy();
            expect(bttnSave.isPresent()).toBeTruthy();
            expect(bttnSave.isDisplayed()).toBeFalsy();
            expect(bttnCancel.isPresent()).toBeTruthy();
            expect(bttnCancel.isDisplayed()).toBeFalsy();
            expect(inptName.isPresent()).toBeTruthy();
            expect(inptName.isDisplayed()).toBeFalsy();
          }
        );

        it('should save changes when button \'Save\' is clicked', function() {
          inptName.sendKeys('Foo');
          expect(inptName.evaluate('modifyValues[rec._source.id]'))
            .toBe('AgraleFoo');
          expect(bttnSave.isEnabled()).toBeTruthy();
          bttnSave.click();
          expect(firstCell.getText()).toBe('AgraleFoo');
          expect(bttnModify.isPresent()).toBeTruthy();
          expect(bttnModify.isDisplayed()).toBeTruthy();
          expect(bttnModify.isEnabled()).toBeTruthy();
          expect(bttnRemove.isPresent()).toBeTruthy();
          expect(bttnRemove.isDisplayed()).toBeTruthy();
          expect(bttnRemove.isEnabled()).toBeTruthy();
          expect(bttnSave.isPresent()).toBeTruthy();
          expect(bttnSave.isDisplayed()).toBeFalsy();
          expect(bttnCancel.isPresent()).toBeTruthy();
          expect(bttnCancel.isDisplayed()).toBeFalsy();
          expect(inptName.isPresent()).toBeTruthy();
          expect(inptName.isDisplayed()).toBeFalsy();
          bttnModify.click();
          inptName.clear();
          inptName.sendKeys('Agrale');
          bttnSave.click();
          expect(firstCell.getText()).toBe('Agrale');
        });

        describe('Validation:', function() {

          var errorArea, errRequired, errTooLong, errNotUnique;

          beforeAll(function() {
            errorArea = firstRow.all(by.tagName('td')).first()
              .element(by.tagName('form'))
              .all(by.tagName('span')).first();
            var errs = errorArea.all(by.tagName('span'));
            errRequired = errs.get(1);
            errTooLong = errs.get(2);
            errNotUnique = errs.get(3);
          });

          it('should not display any error before modification', function() {
            expect(errorArea.isPresent()).toBeTruthy();
            expect(errorArea.isDisplayed()).toBeFalsy();
            expect(errRequired.isPresent()).toBeTruthy();
            expect(errTooLong.isPresent()).toBeTruthy();
            expect(errNotUnique.isPresent()).toBeTruthy();
          });

          it('should check constraint \'required\'', function() {
            inptName.clear();
            expect(errorArea.isDisplayed()).toBeTruthy();
            expect(errRequired.isDisplayed()).toBeTruthy();
            expect(errTooLong.isDisplayed()).toBeFalsy();
            expect(errNotUnique.isDisplayed()).toBeFalsy();
            expect(bttnSave.isEnabled()).toBeFalsy();
          });

          xit('should check constraint \'maxlength\'', function() {
            // This test will fail beacuse 'input' does not allow
            // to exceed 'maxlength'.
            var longString = (new Array(100)).join('x');
            inptName.sendKeys(longString);
            expect(errorArea.isDisplayed()).toBeTruthy();
            expect(errRequired.isDisplayed()).toBeFalsy();
            expect(errTooLong.isDisplayed()).toBeTruthy();
            expect(errNotUnique.isDisplayed()).toBeFalsy();
            expect(bttnSave.isEnabled()).toBeFalsy();
          });

          it('should check constraint \'unique\'', function() {
            inptName.clear();
            inptName.sendKeys('ARO');
            expect(errorArea.isDisplayed()).toBeTruthy();
            expect(errRequired.isDisplayed()).toBeFalsy();
            expect(errTooLong.isDisplayed()).toBeFalsy();
            expect(errNotUnique.isDisplayed()).toBeTruthy();
            expect(bttnSave.isEnabled()).toBeFalsy();
          });

        });

      });

    });

  });

  describe('Model:', function() {

    var bttnCreate, rows, bttnClear, fltrModel, fltrMake;

    beforeAll(function() {
      bttnCreate = element(by.partialLinkText('Create Model'));
      bttnClear = element(by.tiButton('Clear'));
      fltrModel = element(by.id('carmodel'));
      fltrMake = element(by.id('fltrCarmodelMake'));
      rows = element.all(by.repeater('rec in $data'));
    });

    beforeEach(function() {
      browser.get('http://localhost:8080/application/carmodel/list');
    });

    it('should have an initial state', function() {
      expect(bttnCreate.isPresent()).toBeTruthy();
      expect(bttnCreate.isDisplayed()).toBeTruthy();
      expect(bttnCreate.isEnabled()).toBeTruthy();
      expect(bttnClear.isPresent()).toBeTruthy();
      expect(bttnClear.isDisplayed()).toBeTruthy();
      expect(bttnClear.isEnabled()).toBeTruthy();
      expect(fltrModel.isPresent()).toBeTruthy();
      expect(fltrModel.isDisplayed()).toBeTruthy();
      expect(fltrModel.isEnabled()).toBeTruthy();
      expect(fltrMake.isPresent()).toBeTruthy();
      expect(fltrMake.isDisplayed()).toBeTruthy();
      expect(fltrMake.isEnabled()).toBeTruthy();
    });

    it('should open form to create a new \'Model\' when button ' +
      '\'Create Model\' is clicked',
      function() {
        bttnCreate.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carmodel/form');
      }
    );

    describe('Create:', function() {

      var bttnSave, inptName, fltrCarMake, elmCarMake, elmCarMakeOptions;

      beforeAll(function() {
        bttnSave = element(by.tiButton('Save'));
        inptName = element(by.id('carmodel_name'));
        fltrCarMake = element(by.model('carmakeFilter'));
        elmCarMake = element(by.model('carmodel.make.id'));
        elmCarMakeOptions = elmCarMake.all(by.tagName('option'));
      });

      beforeEach(function() {
        browser.get('http://localhost:8080/application/carmodel/form');
      });

      it('should have an initial state', function() {
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeTruthy();
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(inptName.isPresent()).toBeTruthy();
        expect(inptName.isDisplayed()).toBeTruthy();
        expect(inptName.isEnabled()).toBeFalsy();
        expect(fltrCarMake.isPresent()).toBeTruthy();
        expect(fltrCarMake.isDisplayed()).toBeTruthy();
        expect(fltrCarMake.isEnabled()).toBeTruthy();
        expect(elmCarMake.isPresent()).toBeTruthy();
        expect(elmCarMake.isDisplayed()).toBeTruthy();
        expect(elmCarMake.isEnabled()).toBeTruthy();
        expect(elmCarMakeOptions.count()).toBe(271 + 1);
      });

      describe('Filter Car Makes:', function() {

        beforeEach(function() {
          fltrCarMake.clear();
        });

        it('should search by exact value', function() {
          fltrCarMake.sendKeys('Citroen');
          expect(elmCarMakeOptions.count()).toBe(1 + 1);
        });

        it('should search by partial value, case sensitive', function() {
          fltrCarMake.sendKeys('oe');
          expect(elmCarMakeOptions.count()).toBe(2 + 1);
        });

        it('should search by partial value, case insensitive', function() {
          fltrCarMake.sendKeys('OE');
          expect(elmCarMakeOptions.count()).toBe(2 + 1);
        });

      });

      it('should create a new Car Model', function() {
        browser._selectDropdownbyNum(elmCarMake, 256); // Volvo
        inptName.sendKeys('foo');
        expect(bttnSave.isEnabled()).toBeTruthy();
        bttnSave.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carmodel/list');
        // Find the just created Car Make and delete it.
        browser.wait(function() {
          bttnClear.click();
          fltrModel.sendKeys('foo');
          browser._selectDropdownbyNum(fltrMake, 1); // Volvo
          return rows.count().then(
            function(rowCount) {
              return rowCount === 1;
            }
          );
        }, 3000, 'A Car Model \'foo\' has not been created ' +
          'for Car Make \'Volvo\'.');
        var firstRow = rows.first();
        var bttnRemove = firstRow.element(by.tiButton('Remove'));
        bttnRemove.click();
        // A confirmation dialog should be displayed.
        var dlg = element(by.css('.modal-dialog'));
        expect(dlg.isDisplayed()).toBeTruthy();
        var bttnYes = dlg.element(by.partialButtonText('Yes'));
        bttnYes.click();
        // Make sure that Car Make has really been removed.
        expect(dlg.isPresent()).toBeFalsy();
        browser.wait(function() {
          bttnClear.click();
          fltrModel.sendKeys('foo');
          return rows.count().then(
            function(rowCount) {
              return rowCount === 0;
            }
          );
        }, 3000, 'The just created Car Model \'foo\' was not deleted ' +
          'in the Car Make \'Volvo\'.');
      });

      describe('Validation:', function() {

        var errorArea, errRequired, errTooLong, errNotUnique;

        beforeAll(function() {
          errorArea = element(by.name('carmodelForm'))
            .element(by.css('.alert'));
          var errs = errorArea.all(by.tagName('span'));
          errRequired = errs.get(1);
          errTooLong = errs.get(2);
          errNotUnique = errs.get(3);
        });

        it('should not display any error before modification', function() {
          expect(errorArea.isPresent()).toBeTruthy();
          expect(errorArea.isDisplayed()).toBeFalsy();
          expect(errRequired.isPresent()).toBeTruthy();
          expect(errTooLong.isPresent()).toBeTruthy();
          expect(errNotUnique.isPresent()).toBeTruthy();
        });

        it('should check constraint \'required\'', function() {
          browser._selectDropdownbyNum(elmCarMake, 2);
          inptName.sendKeys('foo');
          inptName.clear();
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeTruthy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeFalsy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

        it('should check constraint \'unique\'', function() {
          browser._selectDropdownbyNum(elmCarMake, 256); // Volvo
          inptName.clear();
          inptName.sendKeys('200');
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeFalsy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

      });

    });

    describe('Table:', function() {

      var firstRow, bttnEdit, bttnRemove;

      beforeAll(function() {
        firstRow = rows.first();
        bttnEdit = firstRow.element(by.partialLinkText('Edit'));
        bttnRemove = firstRow.element(by.tiButton('Remove'));
      });

      it('should have an initial state', function() {
        expect(rows.count()).toBe(10);
        expect(bttnEdit.isPresent()).toBeTruthy();
        expect(bttnEdit.isDisplayed()).toBeTruthy();
        expect(bttnEdit.isEnabled()).toBeTruthy();
        expect(bttnRemove.isPresent()).toBeTruthy();
        expect(bttnRemove.isDisplayed()).toBeTruthy();
        expect(bttnRemove.isEnabled()).toBeTruthy();
      });

      it('should display a confirmation dialog when button \'Remove\' ' +
        'is clicked',
        function() {
          var dlg = element(by.css('.modal-dialog'));
          expect(dlg.isPresent()).toBeFalsy();
          bttnRemove.click();
          expect(dlg.isDisplayed()).toBeTruthy();
        }
      );

    });

    describe('Edit:', function() {

      var bttnSave, inptName, fltrCarMake, elmCarMake, elmCarMakeOptions;

      beforeAll(function() {
        bttnSave = element(by.tiButton('Save'));
        inptName = element(by.id('carmodel_name'));
        fltrCarMake = element(by.model('carmakeFilter'));
        elmCarMake = element(by.model('carmodel.make.id'));
        elmCarMakeOptions = elmCarMake.all(by.tagName('option'));
      });

      beforeEach(function() {
        browser.get('http://localhost:8080/application/carmodel/1/form');
      });

      it('should have an initial state', function() {
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeTruthy();
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(inptName.isPresent()).toBeTruthy();
        expect(inptName.isDisplayed()).toBeTruthy();
        expect(inptName.isEnabled()).toBeTruthy();
        expect(fltrCarMake.isPresent()).toBeTruthy();
        expect(fltrCarMake.isDisplayed()).toBeTruthy();
        expect(fltrCarMake.isEnabled()).toBeTruthy();
        expect(elmCarMake.isPresent()).toBeTruthy();
        expect(elmCarMake.isDisplayed()).toBeTruthy();
        expect(elmCarMake.isEnabled()).toBeTruthy();
        expect(elmCarMakeOptions.count())
          .toBe(271); // there is no first blank item
        expect(inptName.evaluate('carmodel.name')).toBe('145');
        expect(elmCarMake.evaluate('carmodel.make.id')).toBe(252); // Volvo
      });

      describe('Filter Car Makes:', function() {

        beforeEach(function() {
          fltrCarMake.clear();
        });

        it('should search by exact value', function() {
          fltrCarMake.sendKeys('Citroen');
          expect(elmCarMakeOptions.count()).toBe(1 + 1);
        });

        it('should search by partial value, case sensitive', function() {
          fltrCarMake.sendKeys('oe');
          expect(elmCarMakeOptions.count()).toBe(2 + 1);
        });

        it('should search by partial value, case insensitive', function() {
          fltrCarMake.sendKeys('OE');
          expect(elmCarMakeOptions.count()).toBe(2 + 1);
        });

      });

      it('should update a Car Model', function() {
        inptName.sendKeys('Foo');
        expect(bttnSave.isEnabled()).toBeTruthy();
        bttnSave.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carmodel/list');
        // Find the just created Car Make and delete it.
        browser.wait(function() {
          bttnClear.click();
          fltrModel.sendKeys('145Foo');
          browser._selectDropdownbyNum(fltrMake, 1); // Volvo
          return rows.count().then(
            function(rowCount) {
              return rowCount === 1;
            }
          );
        }, 3000, 'A Car Model \'145\' has not been updated ' +
          'for Car Make \'Volvo\'.');
        var firstRow = rows.first();
        var bttnEdit = firstRow.element(by.partialLinkText('Edit'));
        bttnEdit.click();
        inptName.clear();
        inptName.sendKeys('145');
        bttnSave.click();
      });

      describe('Validation:', function() {

        var errorArea, errRequired, errTooLong, errNotUnique;

        beforeAll(function() {
          errorArea = element(by.name('carmodelForm'))
            .element(by.css('.alert'));
          var errs = errorArea.all(by.tagName('span'));
          errRequired = errs.get(1);
          errTooLong = errs.get(2);
          errNotUnique = errs.get(3);
        });

        it('should not display any error before modification', function() {
          expect(errorArea.isPresent()).toBeTruthy();
          expect(errorArea.isDisplayed()).toBeFalsy();
          expect(errRequired.isPresent()).toBeTruthy();
          expect(errTooLong.isPresent()).toBeTruthy();
          expect(errNotUnique.isPresent()).toBeTruthy();
        });

        it('should check constraint \'required\'', function() {
          inptName.clear();
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeTruthy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeFalsy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

        it('should check constraint \'unique\'', function() {
          inptName.clear();
          inptName.sendKeys('200');
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeFalsy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });
      });

    });

  });

  describe('Engine:', function() {

    var bttnCreate, rows, bttnClear, fltrEngine, fltrFuelType;

    beforeAll(function() {
      bttnCreate = element(by.partialLinkText('Create Engine'));
      bttnClear = element(by.tiButton('Clear'));
      fltrEngine = element(by.id('carengine'));
      fltrFuelType = element(by.id('fltrCarengineFuelType'));
      rows = element.all(by.repeater('rec in $data'));
    });

    beforeEach(function() {
      browser.get('http://localhost:8080/application/carengine/list');
    });

    it('should have an initial state', function() {
      expect(bttnCreate.isPresent()).toBeTruthy();
      expect(bttnCreate.isDisplayed()).toBeTruthy();
      expect(bttnCreate.isEnabled()).toBeTruthy();
      expect(bttnClear.isPresent()).toBeTruthy();
      expect(bttnClear.isDisplayed()).toBeTruthy();
      expect(bttnClear.isEnabled()).toBeTruthy();
      expect(fltrEngine.isPresent()).toBeTruthy();
      expect(fltrEngine.isDisplayed()).toBeTruthy();
      expect(fltrEngine.isEnabled()).toBeTruthy();
      expect(fltrFuelType.isPresent()).toBeTruthy();
      expect(fltrFuelType.isDisplayed()).toBeTruthy();
      expect(fltrFuelType.isEnabled()).toBeTruthy();
    });

    it('should open form to create a new \'Engine\' when button ' +
      '\'Create Engine\' is clicked',
      function() {
        bttnCreate.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carengine/form');
      }
    );

    describe('Create:', function() {

      var bttnSave, inptName, fltrFuel, elmFuel, elmFuelOptions;

      beforeAll(function() {
        bttnSave = element(by.tiButton('Save'));
        inptName = element(by.id('carengine_enginesize'));
        fltrFuel = element(by.model('carfueltypeFilter'));
        elmFuel = element(by.model('carengine.fuelType.id'));
        elmFuelOptions = elmFuel.all(by.tagName('option'));
      });

      beforeEach(function() {
        browser.get('http://localhost:8080/application/carengine/form');
      });

      it('should have an initial state', function() {
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeTruthy();
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(inptName.isPresent()).toBeTruthy();
        expect(inptName.isDisplayed()).toBeTruthy();
        expect(inptName.isEnabled()).toBeTruthy();
        expect(fltrFuel.isPresent()).toBeTruthy();
        expect(fltrFuel.isDisplayed()).toBeTruthy();
        expect(fltrFuel.isEnabled()).toBeTruthy();
        expect(elmFuel.isPresent()).toBeTruthy();
        expect(elmFuel.isDisplayed()).toBeTruthy();
        expect(elmFuel.isEnabled()).toBeTruthy();
        expect(elmFuelOptions.count()).toBe(2 + 1);
      });

      describe('Filter Fuel Type:', function() {

        beforeEach(function() {
          fltrFuel.clear();
        });

        it('should search by exact value, case sensitive', function() {
          fltrFuel.sendKeys('D');
          expect(elmFuelOptions.count()).toBe(1 + 1);
        });

        it('should search by exact value, case insensitive', function() {
          fltrFuel.sendKeys('d');
          expect(elmFuelOptions.count()).toBe(1 + 1);
        });

      });

      it('should create a new Car Engine', function() {
        browser._selectDropdownbyNum(elmFuel, 1); // D
        inptName.sendKeys('foo');
        expect(bttnSave.isEnabled()).toBeTruthy();
        bttnSave.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carengine/list');
        // Find the just created Car Engine and delete it.
        browser.wait(function() {
          bttnClear.click();
          fltrEngine.sendKeys('foo');
          browser._selectDropdownbyNum(fltrFuelType, 1); // D
          return rows.count().then(
            function(rowCount) {
              return rowCount === 1;
            }
          );
        }, 3000, 'A Car Engine \'foo\' has not been created ' +
          'for Fuel Type \'D\'.');
        var firstRow = rows.first();
        var bttnRemove = firstRow.element(by.tiButton('Remove'));
        bttnRemove.click();
        // A confirmation dialog should be displayed.
        var dlg = element(by.css('.modal-dialog'));
        expect(dlg.isDisplayed()).toBeTruthy();
        var bttnYes = dlg.element(by.partialButtonText('Yes'));
        bttnYes.click();
        // Make sure that Car Make has really been removed.
        expect(dlg.isPresent()).toBeFalsy();
        browser.wait(function() {
          bttnClear.click();
          fltrEngine.sendKeys('foo');
          return rows.count().then(
            function(rowCount) {
              return rowCount === 0;
            }
          );
        }, 3000, 'The just created Car Engine \'foo\' was not deleted ' +
          'for the Fuel Type \'D\'.');
      });

      describe('Validation:', function() {

        var errorArea, errRequired, errTooLong, errNotUnique;

        beforeAll(function() {
          errorArea = element(by.name('carengineForm'))
            .element(by.css('.alert'));
          var errs = errorArea.all(by.tagName('span'));
          errRequired = errs.get(1);
          errTooLong = errs.get(2);
          errNotUnique = errs.get(3);
        });

        it('should not display any error before modification', function() {
          expect(errorArea.isPresent()).toBeTruthy();
          expect(errorArea.isDisplayed()).toBeFalsy();
          expect(errRequired.isPresent()).toBeTruthy();
          expect(errTooLong.isPresent()).toBeTruthy();
          expect(errNotUnique.isPresent()).toBeTruthy();
        });

        it('should check constraint \'required\'', function() {
          inptName.sendKeys('foo');
          inptName.clear();
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeTruthy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeFalsy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

        it('should check constraint \'unique\'', function() {
          inptName.clear();
          inptName.sendKeys('3589');
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeFalsy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

      });

    });

    describe('Table:', function() {

      var firstRow, bttnEdit, bttnRemove;

      beforeAll(function() {
        firstRow = rows.first();
        bttnEdit = firstRow.element(by.partialLinkText('Edit'));
        bttnRemove = firstRow.element(by.tiButton('Remove'));
      });

      it('should have an initial state', function() {
        expect(rows.count()).toBe(10);
        expect(bttnEdit.isPresent()).toBeTruthy();
        expect(bttnEdit.isDisplayed()).toBeTruthy();
        expect(bttnEdit.isEnabled()).toBeTruthy();
        expect(bttnRemove.isPresent()).toBeTruthy();
        expect(bttnRemove.isDisplayed()).toBeTruthy();
        expect(bttnRemove.isEnabled()).toBeTruthy();
      });

      it('should display a confirmation dialog when button \'Remove\' ' +
        'is clicked',
        function() {
          var dlg = element(by.css('.modal-dialog'));
          expect(dlg.isPresent()).toBeFalsy();
          bttnRemove.click();
          expect(dlg.isDisplayed()).toBeTruthy();
        }
      );

    });

    describe('Edit:', function() {

      var bttnSave, inptName, fltrFuel, elmFuel, elmFuelOptions;

      beforeAll(function() {
        bttnSave = element(by.tiButton('Save'));
        inptName = element(by.id('carengine_enginesize'));
        fltrFuel = element(by.model('carfueltypeFilter'));
        elmFuel = element(by.model('carengine.fuelType.id'));
        elmFuelOptions = elmFuel.all(by.tagName('option'));
      });

      beforeEach(function() {
        browser.get('http://localhost:8080/application/carengine/3/form');
      });

      it('should have an initial state', function() {
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeTruthy();
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(inptName.isPresent()).toBeTruthy();
        expect(inptName.isDisplayed()).toBeTruthy();
        expect(inptName.isEnabled()).toBeTruthy();
        expect(fltrFuel.isPresent()).toBeTruthy();
        expect(fltrFuel.isDisplayed()).toBeTruthy();
        expect(fltrFuel.isEnabled()).toBeTruthy();
        expect(elmFuel.isPresent()).toBeTruthy();
        expect(elmFuel.isDisplayed()).toBeTruthy();
        expect(elmFuel.isEnabled()).toBeTruthy();
        expect(elmFuelOptions.count())
          .toBe(2); // there is no first blank item
        expect(inptName.evaluate('carengine.engineSize')).toBe('1.8 L');
        expect(elmFuel.evaluate('carengine.fuelType.id')).toBe(3); // D
      });

      describe('Filter Car Engines:', function() {

        beforeEach(function() {
          fltrFuel.clear();
        });

        it('should search by exact value, case sensitive', function() {
          fltrFuel.sendKeys('D');
          expect(elmFuelOptions.count()).toBe(1);
        });

        it('should search by exact value, case insensitive', function() {
          fltrFuel.sendKeys('d');
          expect(elmFuelOptions.count()).toBe(1);
        });

      });

      it('should update a Car Engine', function() {
        inptName.sendKeys('Foo');
        expect(bttnSave.isEnabled()).toBeTruthy();
        bttnSave.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carengine/list');
        // Find the just created Car Make and delete it.
        browser.wait(function() {
          bttnClear.click();
          fltrEngine.sendKeys('1.8 LFoo');
          browser._selectDropdownbyNum(fltrFuelType, 1); // D
          return rows.count().then(
            function(rowCount) {
              return rowCount === 1;
            }
          );
        }, 3000, 'A Car Engine \'1.8 L\' has not been updated.');
        var firstRow = rows.first();
        var bttnEdit = firstRow.element(by.partialLinkText('Edit'));
        bttnEdit.click();
        inptName.clear();
        inptName.sendKeys('1.8 L');
        bttnSave.click();
      });

      describe('Validation:', function() {

        var errorArea, errRequired, errTooLong, errNotUnique;

        beforeAll(function() {
          errorArea = element(by.name('carengineForm'))
            .element(by.css('.alert'));
          var errs = errorArea.all(by.tagName('span'));
          errRequired = errs.get(1);
          errTooLong = errs.get(2);
          errNotUnique = errs.get(3);
        });

        it('should not display any error before modification', function() {
          expect(errorArea.isPresent()).toBeTruthy();
          expect(errorArea.isDisplayed()).toBeFalsy();
          expect(errRequired.isPresent()).toBeTruthy();
          expect(errTooLong.isPresent()).toBeTruthy();
          expect(errNotUnique.isPresent()).toBeTruthy();
        });

        it('should check constraint \'required\'', function() {
          inptName.clear();
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeTruthy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeFalsy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

        it('should check constraint \'unique\'', function() {
          inptName.clear();
          inptName.sendKeys('3.9/V8');
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeFalsy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });
      });

    });

  });

  describe('Fuel Type:', function() {

    var bttnCreate, rows, bttnClear, fltrFueltype;

    beforeAll(function() {
      bttnCreate = element(by.partialLinkText('Create Fuel Type'));
      bttnClear = element(by.tiButton('Clear'));
      fltrFueltype = element(by.id('fueltype'));
      rows = element.all(by.repeater('rec in $data'));
    });

    beforeEach(function() {
      browser.get('http://localhost:8080/application/carfueltype/list');
    });

    it('should have an initial state', function() {
      expect(bttnCreate.isPresent()).toBeTruthy();
      expect(bttnCreate.isDisplayed()).toBeTruthy();
      expect(bttnCreate.isEnabled()).toBeTruthy();
      expect(bttnClear.isPresent()).toBeTruthy();
      expect(bttnClear.isDisplayed()).toBeTruthy();
      expect(bttnClear.isEnabled()).toBeTruthy();
      expect(fltrFueltype.isPresent()).toBeTruthy();
      expect(fltrFueltype.isDisplayed()).toBeTruthy();
      expect(fltrFueltype.isEnabled()).toBeTruthy();
    });

    it('should open form to create a new \'Fuel Type\' when button ' +
      '\'Create Fuel Type\' is clicked',
      function() {
        bttnCreate.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carfueltype/form');
      }
    );

    describe('Create:', function() {

      var bttnSave, inptName;

      beforeAll(function() {
        bttnSave = element(by.tiButton('Save'));
        inptName = element(by.id('carfueltype_name'));
      });

      beforeEach(function() {
        browser.get('http://localhost:8080/application/carfueltype/form');
      });

      it('should have an initial state', function() {
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeTruthy();
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(inptName.isPresent()).toBeTruthy();
        expect(inptName.isDisplayed()).toBeTruthy();
        expect(inptName.isEnabled()).toBeTruthy();
      });

      it('should create a new Fuel Type', function() {
        inptName.sendKeys('foo');
        expect(bttnSave.isEnabled()).toBeTruthy();
        bttnSave.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/application/carfueltype/list');
        // Find the just created Fuel Type and delete it.
        //
        // Because a datasource for the table is ElasticSearch
        // it is possible that deletion of a Car Make is not
        // reflected immediately (Elasticsearch is a near real time
        // search platform).
        // So we do the check by waiting of the expectation during
        // some time period.
        browser.wait(function() {
          bttnClear.click();
          fltrFueltype.sendKeys('foo');
          return rows.count().then(
            function(rowCount) {
              return rowCount === 1;
            }
          );
        }, 3000, 'A Fuel Type \'foo\' has not been created.');
        var firstRow = rows.first();
        var bttnRemove = firstRow.element(by.tiButton('Remove'));
        bttnRemove.click();
        // A confirmation dialog should be displayed.
        var dlg = element(by.css('.modal-dialog'));
        expect(dlg.isDisplayed()).toBeTruthy();
        var bttnYes = dlg.element(by.partialButtonText('Yes'));
        bttnYes.click();
        // Make sure that Car Make has really been removed.
        expect(dlg.isPresent()).toBeFalsy();
        browser.wait(function() {
          bttnClear.click();
          fltrFueltype.sendKeys('foo');
          return rows.count().then(
            function(rowCount) {
              return rowCount === 0;
            }
          );
        }, 3000, 'The just created Fuel Type \'foo\' was not deleted.');
      });

      describe('Validation:', function() {

        var errorArea, errRequired, errTooLong, errNotUnique;

        beforeAll(function() {
          errorArea = element(by.name('carfueltypeForm'))
            .element(by.css('.alert'));
          var errs = errorArea.all(by.tagName('span'));
          errRequired = errs.get(1);
          errTooLong = errs.get(2);
          errNotUnique = errs.get(3);
        });

        it('should not display any error before modification', function() {
          expect(errorArea.isPresent()).toBeTruthy();
          expect(errorArea.isDisplayed()).toBeFalsy();
          expect(errRequired.isPresent()).toBeTruthy();
          expect(errTooLong.isPresent()).toBeTruthy();
          expect(errNotUnique.isPresent()).toBeTruthy();
        });

        it('should check constraint \'required\'', function() {
          inptName.sendKeys('foo');  // make dirty a form to start validation
          inptName.clear();
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeTruthy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeFalsy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

        xit('should check constraint \'maxlength\'', function() {
          // This test will fail beacuse 'input' does not allow
          // to exceed 'maxlength'.
          var longString = (new Array(100)).join('x');
          inptName.sendKeys(longString);
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeFalsy();
          expect(errTooLong.isDisplayed()).toBeTruthy();
          expect(errNotUnique.isDisplayed()).toBeFalsy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

        it('should check constraint \'unique\'', function() {
          inptName.clear();
          inptName.sendKeys('D');
          expect(errorArea.isDisplayed()).toBeTruthy();
          expect(errRequired.isDisplayed()).toBeFalsy();
          expect(errTooLong.isDisplayed()).toBeFalsy();
          expect(errNotUnique.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
        });

      });

    });

    describe('Table:', function() {

      var firstRow, firstCell, bttnModify, bttnRemove, bttnSave, bttnCancel, inptName;

      beforeAll(function() {
        firstRow = rows.first();
        firstCell = firstRow.all(by.tagName('td')).first()
          .all(by.tagName('span')).first();
        bttnModify = firstRow.element(by.tiButton('Modify'));
        bttnRemove = firstRow.element(by.tiButton('Remove'));
        bttnSave = firstRow.element(by.tiButton('Save'));
        bttnCancel = firstRow.element(by.tiButton('Cancel'));
        inptName = firstRow.element(by.name('carfueltype_name'));
      });

      it('should have an initial state', function() {
        expect(rows.count()).toBe(2);
        expect(bttnModify.isPresent()).toBeTruthy();
        expect(bttnModify.isDisplayed()).toBeTruthy();
        expect(bttnModify.isEnabled()).toBeTruthy();
        expect(bttnRemove.isPresent()).toBeTruthy();
        expect(bttnRemove.isDisplayed()).toBeTruthy();
        expect(bttnRemove.isEnabled()).toBeTruthy();
        expect(bttnSave.isPresent()).toBeTruthy();
        expect(bttnSave.isDisplayed()).toBeFalsy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeFalsy();
        expect(inptName.isPresent()).toBeTruthy();
        expect(inptName.isDisplayed()).toBeFalsy();
      });

      it('should display a confirmation dialog when button \'Remove\' ' +
        'is clicked',
        function() {
          var dlg = element(by.css('.modal-dialog'));
          expect(dlg.isPresent()).toBeFalsy();
          bttnRemove.click();
          expect(dlg.isDisplayed()).toBeTruthy();
        }
      );

      describe('Modify:', function() {

        beforeEach(function() {
          bttnModify.click();
        });

        it('should have an initial state', function() {
          expect(bttnModify.isPresent()).toBeTruthy();
          expect(bttnModify.isDisplayed()).toBeFalsy();
          expect(bttnRemove.isPresent()).toBeTruthy();
          expect(bttnRemove.isDisplayed()).toBeFalsy();
          expect(bttnSave.isPresent()).toBeTruthy();
          expect(bttnSave.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeFalsy();
          expect(bttnCancel.isPresent()).toBeTruthy();
          expect(bttnCancel.isDisplayed()).toBeTruthy();
          expect(bttnCancel.isEnabled()).toBeTruthy();
          expect(inptName.isPresent()).toBeTruthy();
          expect(inptName.isDisplayed()).toBeTruthy();
          expect(inptName.evaluate('modifyValues[rec._source.id]'))
            .toBe('D');
        });

        it('should undo changes when button \'Cancel\' is clicked',
          function() {
            inptName.sendKeys('foo');
            expect(inptName.evaluate('modifyValues[rec._source.id]'))
              .toBe('Dfoo');
            bttnCancel.click();
            expect(firstCell.getText()).toBe('D');
            expect(bttnModify.isPresent()).toBeTruthy();
            expect(bttnModify.isDisplayed()).toBeTruthy();
            expect(bttnModify.isEnabled()).toBeTruthy();
            expect(bttnRemove.isPresent()).toBeTruthy();
            expect(bttnRemove.isDisplayed()).toBeTruthy();
            expect(bttnRemove.isEnabled()).toBeTruthy();
            expect(bttnSave.isPresent()).toBeTruthy();
            expect(bttnSave.isDisplayed()).toBeFalsy();
            expect(bttnCancel.isPresent()).toBeTruthy();
            expect(bttnCancel.isDisplayed()).toBeFalsy();
            expect(inptName.isPresent()).toBeTruthy();
            expect(inptName.isDisplayed()).toBeFalsy();
          }
        );

        it('should save changes when button \'Save\' is clicked', function() {
          inptName.sendKeys('foo');
          expect(inptName.evaluate('modifyValues[rec._source.id]'))
            .toBe('Dfoo');
          expect(bttnSave.isEnabled()).toBeTruthy();
          bttnSave.click();
          expect(firstCell.getText()).toBe('Dfoo');
          expect(bttnModify.isPresent()).toBeTruthy();
          expect(bttnModify.isDisplayed()).toBeTruthy();
          expect(bttnModify.isEnabled()).toBeTruthy();
          expect(bttnRemove.isPresent()).toBeTruthy();
          expect(bttnRemove.isDisplayed()).toBeTruthy();
          expect(bttnRemove.isEnabled()).toBeTruthy();
          expect(bttnSave.isPresent()).toBeTruthy();
          expect(bttnSave.isDisplayed()).toBeFalsy();
          expect(bttnCancel.isPresent()).toBeTruthy();
          expect(bttnCancel.isDisplayed()).toBeFalsy();
          expect(inptName.isPresent()).toBeTruthy();
          expect(inptName.isDisplayed()).toBeFalsy();
          bttnModify.click();
          inptName.clear();
          inptName.sendKeys('D');
          bttnSave.click();
          expect(firstCell.getText()).toBe('D');
        });

        describe('Validation:', function() {

          var errorArea, errRequired, errTooLong, errNotUnique;

          beforeAll(function() {
            errorArea = firstRow.all(by.tagName('td')).first()
              .element(by.tagName('form'))
              .all(by.tagName('span')).first();
            var errs = errorArea.all(by.tagName('span'));
            errRequired = errs.get(1);
            errTooLong = errs.get(2);
            errNotUnique = errs.get(3);
          });

          it('should not display any error before modification', function() {
            expect(errorArea.isPresent()).toBeTruthy();
            expect(errorArea.isDisplayed()).toBeFalsy();
            expect(errRequired.isPresent()).toBeTruthy();
            expect(errTooLong.isPresent()).toBeTruthy();
            expect(errNotUnique.isPresent()).toBeTruthy();
          });

          it('should check constraint \'required\'', function() {
            inptName.clear();
            expect(errorArea.isDisplayed()).toBeTruthy();
            expect(errRequired.isDisplayed()).toBeTruthy();
            expect(errTooLong.isDisplayed()).toBeFalsy();
            expect(errNotUnique.isDisplayed()).toBeFalsy();
            expect(bttnSave.isEnabled()).toBeFalsy();
          });

          xit('should check constraint \'maxlength\'', function() {
            // This test will fail beacuse 'input' does not allow
            // to exceed 'maxlength'.
            var longString = (new Array(100)).join('x');
            inptName.sendKeys(longString);
            expect(errorArea.isDisplayed()).toBeTruthy();
            expect(errRequired.isDisplayed()).toBeFalsy();
            expect(errTooLong.isDisplayed()).toBeTruthy();
            expect(errNotUnique.isDisplayed()).toBeFalsy();
            expect(bttnSave.isEnabled()).toBeFalsy();
          });

          it('should check constraint \'unique\'', function() {
            inptName.clear();
            inptName.sendKeys('G');
            expect(errorArea.isDisplayed()).toBeTruthy();
            expect(errRequired.isDisplayed()).toBeFalsy();
            expect(errTooLong.isDisplayed()).toBeFalsy();
            expect(errNotUnique.isDisplayed()).toBeTruthy();
            expect(bttnSave.isEnabled()).toBeFalsy();
          });

        });

      });

    });

  });

});
