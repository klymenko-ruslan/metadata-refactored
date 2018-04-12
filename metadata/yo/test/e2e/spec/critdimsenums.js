// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

fdescribe('Critical dimensions enumerations:', function() {

  var bttnAddEnum, bttnAddEnumItem, rowsEnums, firstRowEnums,
    rowsEnumItems, firstRowEnumItems, bttnModifyEnum, bttnDeleteEnum,
    bttnSaveEnum, bttnCancelEnum, bttnUndoEnum,
    bttnModifyEnumItem, bttnDeleteEnumItem, bttnSaveEnumItem,
    bttnCancelEnumItem, bttnUndoEnumItem;

  beforeAll(function() {
    bttnAddEnum = element(by.id('bttn-add-enum'));
    bttnAddEnumItem = element(by.id('bttn-add-enum-item'));
    rowsEnums = element(by.id('tbl-enums'))
      .all(by.repeater('row in $data'));
    firstRowEnums = rowsEnums.first();
    rowsEnumItems = element(by.id('tbl-enum-items'))
      .all(by.repeater('row in $data'));
    firstRowEnumItems = rowsEnumItems.first();
    bttnModifyEnum = firstRowEnums.element(by.tiButton('Modify'));
    bttnDeleteEnum = firstRowEnums.element(by.tiButton('Delete'));
    bttnSaveEnum = firstRowEnums.element(by.tiButton('Save'));
    bttnCancelEnum = firstRowEnums.element(by.tiButton('Cancel'));
    bttnUndoEnum = firstRowEnums.element(by.tiButton('Undo'));
    bttnModifyEnumItem = firstRowEnumItems.element(by.tiButton('Modify'));
    bttnDeleteEnumItem = firstRowEnumItems.element(by.tiButton('Delete'));
    bttnSaveEnumItem = firstRowEnumItems.element(by.tiButton('Save'));
    bttnCancelEnumItem = firstRowEnumItems.element(by.tiButton('Cancel'));
    bttnUndoEnumItem = firstRowEnumItems.element(by.tiButton('Undo'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/criticaldimension/enums');
  });

  describe('enumerations:', function() {

    it('should have an initial state', function() {
      expect(rowsEnums.count()).toBe(10);
      expect(bttnAddEnum.isPresent()).toBeTruthy();
      expect(bttnAddEnum.isDisplayed()).toBeTruthy();
      expect(bttnAddEnum.isEnabled()).toBeTruthy();
      expect(bttnModifyEnum.isPresent()).toBeTruthy();
      expect(bttnModifyEnum.isDisplayed()).toBeTruthy();
      expect(bttnModifyEnum.isEnabled()).toBeTruthy();
      expect(bttnDeleteEnum.isPresent()).toBeTruthy();
      expect(bttnDeleteEnum.isDisplayed()).toBeTruthy();
      expect(bttnDeleteEnum.isEnabled()).toBeTruthy();
      expect(bttnSaveEnum.isPresent()).toBeTruthy();
      expect(bttnSaveEnum.isDisplayed()).toBeFalsy();
      expect(bttnCancelEnum.isPresent()).toBeTruthy();
      expect(bttnCancelEnum.isDisplayed()).toBeFalsy();
      expect(bttnUndoEnum.isPresent()).toBeTruthy();
      expect(bttnUndoEnum.isDisplayed()).toBeFalsy();
    });

    it('should display items for the selected enum', function() {
      expect(rowsEnumItems.count()).toBe(2);
      var secondRowEnum = rowsEnums.get(1);
      secondRowEnum.click();
      expect(rowsEnumItems.count()).toBe(10);
      firstRowEnums.click();
      expect(rowsEnumItems.count()).toBe(2);
    });

    describe('create new:', function() {

      var dlgNewEnum, dlgDelEnum, inptName, bttnCreate, bttnCancel;

      beforeAll(function() {
        dlgNewEnum = element(by.id('addEnumDlg'));
        dlgDelEnum = element(by.id('delEnumDlg'));
        inptName = dlgNewEnum.element(by.id('addEnumDlgCtrlName'));
        bttnCreate = dlgNewEnum.element(by.partialButtonText('Create'));
        bttnCancel = dlgNewEnum.element(by.partialButtonText('Cancel'));
      });

      beforeEach(function() {
        bttnAddEnum.click();
      });

      it('should have an initial state', function() {
        expect(dlgNewEnum.isPresent()).toBeTruthy();
        // We expect in the line below that dialog to create
        // a new enumeration is displayed because it is opened in
        // the function beforeEach() above.
        expect(dlgNewEnum.isDisplayed()).toBeTruthy();
        expect(dlgDelEnum.isPresent()).toBeTruthy();
        expect(dlgDelEnum.isDisplayed()).toBeFalsy();
        expect(bttnCreate.isPresent()).toBeTruthy();
        expect(bttnCreate.isDisplayed()).toBeTruthy();
        expect(bttnCreate.isEnabled()).toBeFalsy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeTruthy();
        expect(bttnCancel.isEnabled()).toBeTruthy();
      });

      it('should close the dialog when button \'Cancel\' is clicked',
        function() {
          expect(dlgNewEnum.isDisplayed()).toBeTruthy();
          bttnCancel.click();
          expect(dlgNewEnum.isDisplayed()).toBeFalsy();
        }
      );

      it('should not allow to create a new enum with non-unique name',
        function() {
          inptName.sendKeys('CL_Typ');
          expect(bttnCreate.isEnabled()).toBeFalsy();
        }
      );

      it('should allow to create a new enum and then delete it', function() {
        // Create a new enum.
        inptName.sendKeys('foo');
        expect(bttnCreate.isEnabled()).toBeTruthy();
        bttnCreate.click();
        expect(dlgNewEnum.isDisplayed()).toBeFalsy();
        // Check that enum is displayed in the table.
        var fooRowEnum = rowsEnums.get(7);
        var firstCell = fooRowEnum.all(by.tagName('td'))
          .first().all(by.tagName('span')).first();
        expect(firstCell.isPresent());
        expect(firstCell.isDisplayed());
        expect(firstCell.getText()).toBe('foo');
        // Delete the enum.
        var bttnDeleteFoo = fooRowEnum.all(by.tagName('td')).last()
          .element(by.tiButton('Delete'));
        expect(bttnDeleteFoo.isPresent()).toBeTruthy();
        expect(bttnDeleteFoo.isDisplayed()).toBeTruthy();
        expect(bttnDeleteFoo.isEnabled()).toBeTruthy();
        bttnDeleteFoo.click();
        expect(dlgDelEnum.isDisplayed()).toBeTruthy();
        var bttnConfirmDelete = dlgDelEnum.element(by.tiButton('Delete'));
        expect(bttnConfirmDelete.isPresent()).toBeTruthy();
        expect(bttnConfirmDelete.isDisplayed()).toBeTruthy();
        expect(bttnConfirmDelete.isEnabled()).toBeTruthy();
        var bttnCancelDelete = dlgDelEnum.element(by.tiButton('Cancel'));
        expect(bttnCancelDelete.isPresent()).toBeTruthy();
        expect(bttnCancelDelete.isDisplayed()).toBeTruthy();
        expect(bttnCancelDelete.isEnabled()).toBeTruthy();
        // Check that button 'Cancel' closes the dialog.
        bttnCancelDelete.click();
        expect(dlgDelEnum.isDisplayed()).toBeFalsy();
        // Press the button 'Delete' in the table again and confirm
        // deletion of the enum.
        bttnDeleteFoo.click();
        bttnConfirmDelete.click();
        // The confirmation dialog should disappear and a row with enum
        // is removed from the table.
        expect(dlgDelEnum.isDisplayed()).toBeFalsy();
        expect(firstCell.getText()).not.toBe('foo');
      });

    });

    describe('edit:', function() {

      var firstCellTxt, firstCellInput;

      beforeAll(function() {
        var firstCell = firstRowEnums.all(by.tagName('td')).first();
        firstCellTxt = firstCell.all(by.tagName('span')).first();
        firstCellInput = firstCell.all(by.tagName('input')).first();
      });

      beforeEach(function() {
        bttnModifyEnum.click();
      });

      it('should have an initial state',
        function() {
          expect(firstCellTxt.isDisplayed()).toBeFalsy();
          expect(firstCellInput.isDisplayed()).toBeTruthy();
          expect(bttnModifyEnum.isDisplayed()).toBeFalsy();
          expect(bttnDeleteEnum.isDisplayed()).toBeFalsy();
          expect(bttnSaveEnum.isDisplayed()).toBeTruthy();
          expect(bttnSaveEnum.isEnabled()).toBeFalsy();
          expect(bttnUndoEnum.isDisplayed()).toBeTruthy();
          expect(bttnUndoEnum.isEnabled()).toBeFalsy();
          expect(bttnCancelEnum.isDisplayed()).toBeTruthy();
          expect(bttnCancelEnum.isEnabled()).toBeTruthy();
        }
      );

      it('should revert to view record mode ' +
        'when button \'Cancel\' is clicked',
        function() {
          bttnCancelEnum.click();
          expect(firstCellTxt.isDisplayed()).toBeTruthy();
          expect(firstCellInput.isDisplayed()).toBeFalsy();
          expect(bttnModifyEnum.isDisplayed()).toBeTruthy();
          expect(bttnModifyEnum.isEnabled()).toBeTruthy();
          expect(bttnDeleteEnum.isDisplayed()).toBeTruthy();
          expect(bttnDeleteEnum.isEnabled()).toBeTruthy();
          expect(bttnSaveEnum.isDisplayed()).toBeFalsy();
          expect(bttnUndoEnum.isDisplayed()).toBeFalsy();
          expect(bttnCancelEnum.isDisplayed()).toBeFalsy();
        }
      );

      it('should allow to undo changes', function() {
        expect(firstCellInput.evaluate('cdeModifying.name'))
          .toBe('yesNoEnum');
        firstCellInput.sendKeys('Foo');
        expect(firstCellInput.evaluate('cdeModifying.name'))
          .toBe('yesNoEnumFoo');
        expect(bttnSaveEnum.isEnabled()).toBeTruthy();
        expect(bttnUndoEnum.isEnabled()).toBeTruthy();
        bttnUndoEnum.click();
        expect(firstCellInput.evaluate('cdeModifying.name'))
          .toBe('yesNoEnum');
      });

      it('should allow to save changes', function() {
        // Do a modification.
        expect(firstCellInput.evaluate('cdeModifying.name'))
          .toBe('yesNoEnum');
        firstCellInput.sendKeys('Foo');
        expect(firstCellInput.evaluate('cdeModifying.name'))
          .toBe('yesNoEnumFoo');
        expect(bttnSaveEnum.isEnabled()).toBeTruthy();
        // Save the modification.
        bttnSaveEnum.click();
        // Check that modified value is displayed in the table row.
        expect(firstCellTxt.isDisplayed()).toBeTruthy();
        expect(firstCellInput.isDisplayed()).toBeFalsy();
        expect(bttnModifyEnum.isDisplayed()).toBeTruthy();
        expect(bttnModifyEnum.isEnabled()).toBeTruthy();
        expect(bttnDeleteEnum.isDisplayed()).toBeTruthy();
        expect(bttnDeleteEnum.isEnabled()).toBeTruthy();
        expect(bttnSaveEnum.isDisplayed()).toBeFalsy();
        expect(bttnUndoEnum.isDisplayed()).toBeFalsy();
        expect(bttnCancelEnum.isDisplayed()).toBeFalsy();
        expect(firstCellTxt.getText()).toBe('yesNoEnumFoo');
        // Do modification again to revert the old value.
        bttnModifyEnum.click();
        firstCellInput.clear();
        firstCellInput.sendKeys('yesNoEnum');
        bttnSaveEnum.click();
        expect(firstCellTxt.getText()).toBe('yesNoEnum');
      });

      describe('validation:', function() {

        it('should not allow to save modifications with empty', function() {
          firstCellInput.clear();
          expect(bttnSaveEnum.isEnabled()).toBeFalsy();
        });

        it('should not allow to save modifications with non-uniqe name',
          function() {
            firstCellInput.clear();
            firstCellInput.sendKeys('CL_Typ');
            expect(bttnSaveEnum.isEnabled()).toBeFalsy();
          }
        );

      });

    });

  });

  describe('items:', function() {

    it('should have an initial state', function() {
      expect(rowsEnumItems.count()).toBe(2);
      expect(bttnAddEnumItem.isPresent()).toBeTruthy();
      expect(bttnAddEnumItem.isDisplayed()).toBeTruthy();
      expect(bttnAddEnumItem.isEnabled()).toBeTruthy();
      expect(bttnModifyEnumItem.isPresent()).toBeTruthy();
      expect(bttnModifyEnumItem.isDisplayed()).toBeTruthy();
      expect(bttnModifyEnumItem.isEnabled()).toBeTruthy();
      expect(bttnDeleteEnumItem.isPresent()).toBeTruthy();
      expect(bttnDeleteEnumItem.isDisplayed()).toBeTruthy();
      expect(bttnDeleteEnumItem.isEnabled()).toBeTruthy();
      expect(bttnSaveEnumItem.isPresent()).toBeTruthy();
      expect(bttnSaveEnumItem.isDisplayed()).toBeFalsy();
      expect(bttnCancelEnumItem.isPresent()).toBeTruthy();
      expect(bttnCancelEnumItem.isDisplayed()).toBeFalsy();
      expect(bttnUndoEnumItem.isPresent()).toBeTruthy();
      expect(bttnUndoEnumItem.isDisplayed()).toBeFalsy();
    });

    describe('create:', function() {

      var dlgNewEnumItem, dlgDelEnumItem, inptEnumItemName, bttnCreate,
        bttnCancel;

      beforeAll(function() {
        dlgNewEnumItem = element(by.id('addEnumItmDlg'));
        dlgDelEnumItem = element(by.id('delEnumItmDlg'));
        inptEnumItemName = dlgNewEnumItem.element(
          by.id('addEnumItmDlgCtrlName'));
        bttnCreate = dlgNewEnumItem.element(by.partialButtonText('Create'));
        bttnCancel = dlgNewEnumItem.element(by.partialButtonText('Cancel'));
      });

      beforeEach(function() {
        bttnAddEnumItem.click();
      });

      it('should have an initial state', function() {
        expect(dlgNewEnumItem.isPresent()).toBeTruthy();
        // We expect in the line below that dialog to create
        // a new enumeration item is displayed because it is opened in
        // the function beforeEach() above.
        expect(dlgNewEnumItem.isDisplayed()).toBeTruthy();
        expect(dlgDelEnumItem.isPresent()).toBeTruthy();
        expect(dlgDelEnumItem.isDisplayed()).toBeFalsy();
        expect(bttnCreate.isPresent()).toBeTruthy();
        expect(bttnCreate.isDisplayed()).toBeTruthy();
        expect(bttnCreate.isEnabled()).toBeFalsy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeTruthy();
        expect(bttnCancel.isEnabled()).toBeTruthy();
      });

      it('should close the dialog when button \'Cancel\' is clicked',
        function() {
          expect(dlgNewEnumItem.isDisplayed()).toBeTruthy();
          bttnCancel.click();
          expect(dlgNewEnumItem.isDisplayed()).toBeFalsy();
        }
      );

      it('should not allow to create a new enum item with non-unique name',
        function() {
          inptEnumItemName.sendKeys('YES');
          expect(bttnCreate.isEnabled()).toBeFalsy();
        }
      );

      it('should allow to create a new enum item and then delete it',
        function() {
          // Create a new enum.
          inptEnumItemName.sendKeys('foo');
          expect(bttnCreate.isEnabled()).toBeTruthy();
          bttnCreate.click();
          expect(dlgNewEnumItem.isDisplayed()).toBeFalsy();
          // Check that enum item is displayed in the table.
          var lastRowEnumItem = rowsEnumItems.last();
          var firstCell = lastRowEnumItem.all(by.tagName('td'))
            .first().all(by.tagName('span')).first();
          expect(firstCell.isPresent());
          expect(firstCell.isDisplayed());
          expect(firstCell.getText()).toBe('foo');
          // Delete the enum.
          var bttnDeleteFoo = lastRowEnumItem.all(by.tagName('td')).last()
            .element(by.tiButton('Delete'));
          expect(bttnDeleteFoo.isPresent()).toBeTruthy();
          expect(bttnDeleteFoo.isDisplayed()).toBeTruthy();
          expect(bttnDeleteFoo.isEnabled()).toBeTruthy();
          bttnDeleteFoo.click();
          expect(dlgDelEnumItem.isDisplayed()).toBeTruthy();
          var bttnConfirmDelete = dlgDelEnumItem.element(by.tiButton('Delete'));
          expect(bttnConfirmDelete.isPresent()).toBeTruthy();
          expect(bttnConfirmDelete.isDisplayed()).toBeTruthy();
          expect(bttnConfirmDelete.isEnabled()).toBeTruthy();
          var bttnCancelDelete = dlgDelEnumItem.element(by.tiButton('Cancel'));
          expect(bttnCancelDelete.isPresent()).toBeTruthy();
          expect(bttnCancelDelete.isDisplayed()).toBeTruthy();
          expect(bttnCancelDelete.isEnabled()).toBeTruthy();
          // Check that button 'Cancel' closes the dialog.
          bttnCancelDelete.click();
          expect(dlgDelEnumItem.isDisplayed()).toBeFalsy();
          // Press the button 'Delete' in the table again and confirm
          // deletion of the enum item.
          bttnDeleteFoo.click();
          bttnConfirmDelete.click();
          // The confirmation dialog should disappear and a row with enum item
          // is removed from the table.
          expect(dlgDelEnumItem.isDisplayed()).toBeFalsy();
          expect(firstCell.getText()).not.toBe('foo');
        }
      );

    });

    describe('edit:', function() {

      var firstCellTxt, firstCellInput;

      beforeAll(function() {
        var firstCell = firstRowEnumItems.all(by.tagName('td')).first();
        firstCellTxt = firstCell.all(by.tagName('span')).first();
        firstCellInput = firstCell.all(by.tagName('input')).first();
      });

      beforeEach(function() {
        bttnModifyEnumItem.click();
      });

      it('should have an initial state',
        function() {
          expect(firstCellTxt.isDisplayed()).toBeFalsy();
          expect(firstCellInput.isDisplayed()).toBeTruthy();
          expect(bttnModifyEnumItem.isDisplayed()).toBeFalsy();
          expect(bttnDeleteEnumItem.isDisplayed()).toBeFalsy();
          expect(bttnSaveEnumItem.isDisplayed()).toBeTruthy();
          expect(bttnSaveEnumItem.isEnabled()).toBeFalsy();
          expect(bttnUndoEnumItem.isDisplayed()).toBeTruthy();
          expect(bttnUndoEnumItem.isEnabled()).toBeFalsy();
          expect(bttnCancelEnumItem.isDisplayed()).toBeTruthy();
          expect(bttnCancelEnumItem.isEnabled()).toBeTruthy();
        }
      );

      it('should revert to view record mode ' +
        'when button \'Cancel\' is clicked',
        function() {
          bttnCancelEnumItem.click();
          expect(firstCellTxt.isDisplayed()).toBeTruthy();
          expect(firstCellInput.isDisplayed()).toBeFalsy();
          expect(bttnModifyEnum.isDisplayed()).toBeTruthy();
          expect(bttnModifyEnum.isEnabled()).toBeTruthy();
          expect(bttnDeleteEnum.isDisplayed()).toBeTruthy();
          expect(bttnDeleteEnum.isEnabled()).toBeTruthy();
          expect(bttnSaveEnum.isDisplayed()).toBeFalsy();
          expect(bttnUndoEnum.isDisplayed()).toBeFalsy();
          expect(bttnCancelEnum.isDisplayed()).toBeFalsy();
        }
      );

      it('should allow to undo changes', function() {
        expect(firstCellInput.evaluate('cdevModifying.val'))
          .toBe('YES');
        firstCellInput.sendKeys('foo');
        expect(firstCellInput.evaluate('cdevModifying.val'))
          .toBe('YESfoo');
        expect(bttnSaveEnumItem.isEnabled()).toBeTruthy();
        expect(bttnUndoEnumItem.isEnabled()).toBeTruthy();
        bttnUndoEnumItem.click();
        expect(firstCellInput.evaluate('cdevModifying.val'))
          .toBe('YES');
      });

      it('should allow to save changes', function() {
        // Do a modification.
        expect(firstCellInput.evaluate('cdevModifying.val'))
          .toBe('YES');
        firstCellInput.sendKeys('foo');
        expect(firstCellInput.evaluate('cdevModifying.val'))
          .toBe('YESfoo');
        expect(bttnSaveEnumItem.isEnabled()).toBeTruthy();
        // Save the modification.
        bttnSaveEnumItem.click();
        // Check that modified value is displayed in the table row.
        expect(firstCellTxt.isDisplayed()).toBeTruthy();
        expect(firstCellInput.isDisplayed()).toBeFalsy();
        expect(bttnModifyEnumItem.isDisplayed()).toBeTruthy();
        expect(bttnModifyEnumItem.isEnabled()).toBeTruthy();
        expect(bttnDeleteEnumItem.isDisplayed()).toBeTruthy();
        expect(bttnDeleteEnumItem.isEnabled()).toBeTruthy();
        expect(bttnSaveEnumItem.isDisplayed()).toBeFalsy();
        expect(bttnUndoEnumItem.isDisplayed()).toBeFalsy();
        expect(bttnCancelEnumItem.isDisplayed()).toBeFalsy();
        expect(firstCellTxt.getText()).toBe('YESfoo');
        // Do modification again to revert the old value.
        bttnModifyEnumItem.click();
        firstCellInput.clear();
        firstCellInput.sendKeys('YES');
        bttnSaveEnumItem.click();
        expect(firstCellTxt.getText()).toBe('YES');
      });

      describe('validation:', function() {

        it('should not allow to save modifications with empty', function() {
          firstCellInput.clear();
          expect(bttnSaveEnumItem.isEnabled()).toBeFalsy();
        });

        it('should not allow to save modifications with non-uniqe name',
          function() {
            firstCellInput.clear();
            firstCellInput.sendKeys('NO');
            expect(bttnSaveEnumItem.isEnabled()).toBeFalsy();
          }
        );

      });

    });

  });

});
