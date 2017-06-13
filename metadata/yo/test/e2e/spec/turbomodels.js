// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

var EC = protractor.ExpectedConditions;

describe('Turbo Models:', function() {

  var elmManufacturer, elmClear, elmTurboType, elmTurboModel;

  beforeAll(function() {
    elmManufacturer = element(by.model('selection.id'));
    elmClear = element(by.tagName('picker')).element(by.tagName('button'));
    elmTurboType = element(by.model('selection.turboType'));
    elmTurboModel = element(by.model('selection.turboModel'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/other/turboModels');
  });

  it('should have an initial state', function() {
    expect(elmManufacturer.isPresent()).toBeTruthy();
    expect(elmManufacturer.isDisplayed()).toBeTruthy();
    expect(elmManufacturer.isEnabled()).toBeTruthy();
    expect(elmClear.isPresent()).toBeTruthy();
    expect(elmClear.isDisplayed()).toBeTruthy();
    expect(elmClear.isEnabled()).toBeFalsy();
    expect(elmTurboType.isPresent()).toBeTruthy();
    expect(elmTurboType.isDisplayed()).toBeTruthy();
    expect(elmTurboType.isEnabled()).toBeTruthy();
    expect(elmTurboModel.isPresent()).toBeTruthy();
    expect(elmTurboModel.isDisplayed()).toBeTruthy();
    expect(elmTurboModel.isEnabled()).toBeTruthy();
    expect(elmTurboType.evaluate('turboTypes')).toBeNull();
    expect(elmTurboModel.evaluate('turboModels')).toBeNull();
  });

  it('should clear all selection when icon \'close\' is clicked', function() {
    browser._selectDropdownbyNum(elmManufacturer, 3); // Holset
    browser._selectDropdownbyNum(elmTurboType, 2);    // Turbo Type:  H1E
    expect(elmTurboModel.evaluate('turboModels.length'))
      .toBe(1); // Turbo Model: H1E
    expect(elmClear.isEnabled()).toBeTruthy();
    elmClear.click();
    expect(elmManufacturer.evaluate('selection.id')).toBeNull();
    expect(elmTurboType.evaluate('turboTypes')).toBeNull();
    expect(elmTurboModel.evaluate('turboModels')).toBeNull();
  });

  describe('turbo type:', function() {

    var fltrTurboType, bttnCreateType, bttnRenameType, bttnDeleteType;

    beforeAll(function() {
      fltrTurboType = element(by.model('turboTypeFilter'));
      bttnCreateType = element(by.partialLinkText('Create Type'));
      bttnRenameType = element(by.partialLinkText('Rename Type'));
      bttnDeleteType = element(by.partialLinkText('Delete Type'));
    });

    it('should have an initial state', function() {
      expect(fltrTurboType.isPresent()).toBeTruthy();
      expect(fltrTurboType.isDisplayed()).toBeTruthy();
      expect(fltrTurboType.isEnabled()).toBeTruthy();
      expect(elmTurboType.evaluate('turboModels.length')).toBeNull();
      expect(bttnCreateType.isPresent()).toBeTruthy();
      expect(bttnCreateType.isDisplayed()).toBeTruthy();
      expect(bttnCreateType.isEnabled()).toBeTruthy();
      expect(bttnRenameType.isPresent()).toBeTruthy();
      expect(bttnRenameType.isDisplayed()).toBeTruthy();
      //expect(bttnRenameType.isEnabled()).toBeFalsy();
      expect(bttnDeleteType .isPresent()).toBeTruthy();
      expect(bttnDeleteType .isDisplayed()).toBeTruthy();
      //expect(bttnDeleteType .isEnabled()).toBeFalsy();
    });

    describe('filter by name:', function() {

      var ttOpts;

      beforeAll(function() {
        ttOpts = elmTurboType.all(by.tagName('option'));
      });

      beforeEach(function() {
        browser._selectDropdownbyNum(elmManufacturer, 3); // Holset
      });

      it('should display only turbo types for the selected manufacturer',
        function() {
          expect(elmTurboType.evaluate('turboTypes.length')).toBe(7);
          expect(ttOpts.count()).toBe(8);
        }
      );

      it('should filter by exact val', function() {
        fltrTurboType.sendKeys('H1E');
        expect(ttOpts.count()).toBe(2); // first option is empty
      });

      it('should filter by partial val, case sensitively', function() {
        fltrTurboType.sendKeys('C');
        expect(ttOpts.count()).toBe(3); // first option is empty
      });

      it('should filter by partial val, case insensitively', function() {
        fltrTurboType.sendKeys('c');
        expect(ttOpts.count()).toBe(3); // first option is empty
      });

    });

    describe('create:', function() {

      var dlgCreateTurboType, bttnCancel, bttnCreate, inputName;

      beforeAll(function() {
        dlgCreateTurboType = element(by.css('.modal-dialog'));
        bttnCancel = dlgCreateTurboType.element(
          by.partialButtonText('Cancel'));
        bttnCreate = dlgCreateTurboType.element(
          by.partialButtonText('Create'));
        inputName = element(by.model('name'));
      });

      beforeEach(function() {
        browser._selectDropdownbyNum(elmManufacturer, 3); // Holset
        bttnCreateType.click();
      });

      it('should have an initial state', function() {
        // Check that a dialog to create a new turbo type is displayed.
        expect(dlgCreateTurboType.isPresent()).toBeTruthy();
        expect(dlgCreateTurboType.isDisplayed()).toBeTruthy();
        expect(inputName.isPresent()).toBeTruthy();
        expect(inputName.isDisplayed()).toBeTruthy();
        expect(inputName.isEnabled()).toBeTruthy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeTruthy();
        expect(bttnCancel.isEnabled()).toBeTruthy();
        expect(bttnCreate.isPresent()).toBeTruthy();
        expect(bttnCreate.isDisplayed()).toBeTruthy();
        expect(bttnCreate.isEnabled()).toBeTruthy();
      });

      it('should close the dilog when button \'Cancel\' is clicked',
        function() {
          bttnCancel.click();
          expect(dlgCreateTurboType.isPresent()).toBeFalsy();
        }
      );

      it('should create a new turbo type', function() {
        inputName.sendKeys('FOO');
        expect(bttnCreate.isPresent()).toBeTruthy();
        expect(bttnCreate.isDisplayed()).toBeTruthy();
        expect(bttnCreate.isEnabled()).toBeTruthy();
        bttnCreate.click();
        // The dialog should be hidden.
        browser.wait(EC.invisibilityOf(dlgCreateTurboType), 1000,
          'A dialog to create a new turbo type was not hidden.');
        elmTurboType.evaluate('turboTypes').then(function(turboTypes) {
          var found =  false;
          for(var tt of turboTypes) {
            if (tt.name === 'FOO') {
              found = true;
              break;
            }
          }
          expect(found).toBeTruthy();
        });
      });

      // TODO: validators

    });

    describe('rename:', function() {

      var dlgRenameTurboType, bttnCancel, bttnRename, inputName;

      beforeAll(function() {
        dlgRenameTurboType = element(by.css('.modal-dialog'));
        bttnCancel = dlgRenameTurboType.element(
          by.partialButtonText('Cancel'));
        bttnRename = dlgRenameTurboType.element(
          by.partialButtonText('Rename'));
        inputName = element(by.model('name'));
      });

      beforeEach(function() {
        browser._selectDropdownbyNum(elmManufacturer, 3); // Holset
        browser._selectDropdownbyNum(elmTurboType, 1); // FOO
        bttnRenameType.click();
      });

      it('should close the dilog when button \'Cancel\' is clicked',
        function() {
          bttnCancel.click();
          expect(dlgRenameTurboType.isPresent()).toBeFalsy();
        }
      );

      it('should have an initial state', function() {
        // Check that a dialog to create a new turbo type is displayed.
        expect(dlgRenameTurboType.isPresent()).toBeTruthy();
        expect(dlgRenameTurboType.isDisplayed()).toBeTruthy();
        expect(inputName.isPresent()).toBeTruthy();
        expect(inputName.isDisplayed()).toBeTruthy();
        expect(inputName.isEnabled()).toBeTruthy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeTruthy();
        expect(bttnCancel.isEnabled()).toBeTruthy();
        expect(bttnRename.isPresent()).toBeTruthy();
        expect(bttnRename.isDisplayed()).toBeTruthy();
        expect(bttnRename.isEnabled()).toBeTruthy();
        expect(elmTurboType.evaluate('selection.turboType.name')).toBe('FOO');
      });

      it('should rename a turbo type', function() {
        inputName.clear(); // clear 'FOO'
        inputName.sendKeys('FO');
        bttnRename.click();
        // The dialog should be hidden.
        browser.wait(EC.invisibilityOf(dlgRenameTurboType), 1000,
          'A dialog to rename a turbo type was not hidden.');
        elmTurboType.evaluate('turboTypes').then(function(turboTypes) {
          var found =  false;
          for(var tt of turboTypes) {
            if (tt.name === 'FO') {
              found = true;
              break;
            }
          }
          expect(found).toBeTruthy();
        });
      });

      // TODO: validators

    });

    describe('delete:', function() {

      var dlgDeleteTurboType, bttnNo, bttnYes, dlgDeleteTurboTypeFailure,
        bttnClose;

      beforeAll(function() {
        dlgDeleteTurboType = element(by.css('.modal-dialog'));
        bttnNo = dlgDeleteTurboType.element(
          by.partialButtonText('No'));
        bttnYes = dlgDeleteTurboType.element(
          by.partialButtonText('Yes'));
        dlgDeleteTurboTypeFailure = element(by.css('.modal-dialog'));
        bttnClose = dlgDeleteTurboTypeFailure.element(
          by.partialButtonText('Close'));
      });

      beforeEach(function() {
        browser._selectDropdownbyNum(elmManufacturer, 3); // Holset
      });

      it('should close the dilog when button \'No\' is clicked',
        function() {
          browser._selectDropdownbyNum(elmTurboType, 1); // FO
          expect(bttnDeleteType.isEnabled()).toBeTruthy();
          bttnDeleteType.click();
          expect(dlgDeleteTurboType.isDisplayed()).toBeTruthy();
          bttnNo.click();
          expect(dlgDeleteTurboType.isPresent()).toBeFalsy();
        }
      );

      it('should allow to delete a turbo type', function() {
        browser._selectDropdownbyNum(elmTurboType, 1); // FO
        expect(elmTurboType.evaluate('selection.turboType.name')).toBe('FO');
        expect(bttnDeleteType.isEnabled()).toBeTruthy();
        bttnDeleteType.click();
        expect(dlgDeleteTurboType.isDisplayed()).toBeTruthy();
        bttnYes.click();
        // The dialog should be hidden.
        browser.wait(EC.invisibilityOf(dlgDeleteTurboType), 1000,
          'A dialog to delete a turbo type was not hidden.');
        elmTurboType.evaluate('turboTypes').then(function(turboTypes) {
          var found =  false;
          for(var tt of turboTypes) {
            if (tt.name === 'FO') {
              found = true;
              break;
            }
          }
          expect(found).toBeFalsy();
        });
      });

      it('should not allow to delete a turbo type which is in use',
        function() {
          browser._selectDropdownbyNum(elmTurboType, 1); // H1C
          expect(elmTurboType.evaluate('selection.turboType.name')).toBe('H1C');
          expect(bttnDeleteType.isEnabled()).toBeTruthy();
          bttnDeleteType.click();
          expect(dlgDeleteTurboType.isDisplayed()).toBeTruthy();
          bttnYes.click();
          // A dialog with message:
          //   Turbo type must not be used for any parts or turbo models.
          //   Check server log for details.
          // should be displayed.
          expect(dlgDeleteTurboTypeFailure.isDisplayed()).toBeTruthy();
          bttnClose.click();
          expect(dlgDeleteTurboTypeFailure.isPresent()).toBeFalsy();
          elmTurboType.evaluate('turboTypes').then(function(turboTypes) {
            var found =  false;
            for(var tt of turboTypes) {
              if (tt.name === 'H1C') {
                found = true;
                break;
              }
            }
            expect(found).toBeTruthy();
          });
        }
      );

    });
  });

});
