// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

var path = require('path');
var EC = protractor.ExpectedConditions;

describe('Changelog Sources:', function() {

  var rows, firstRowTds, bttnView, bttnRemove, bttnCreateSource, fltrName,
    fltrDescription, fltrUrl, fltrSourceName, bttnClear, bttnRefresh, dlg;

  beforeAll(function() {
    rows = element.all(by.repeater('rec in $data'));
    firstRowTds = rows.first().all(by.tagName('td'));
    var tdActions = firstRowTds.last();
    bttnCreateSource = element(by.tiButton('Create Source'));
    bttnView = tdActions.element(by.tiButton('View'));
    bttnClear = element(by.tiButton('Clear'));
    bttnRefresh = element(by.tiButton('Refresh'));
    bttnRemove = element(by.tiButton('Remove'));
    fltrName = element(by.id('fltrName'));
    fltrDescription = element(by.id('fltrDescription'));
    fltrUrl = element(by.id('fltrUrl'));
    fltrSourceName = element(by.id('fltrSourceName'));
    dlg = element(by.css('.modal-dialog'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/changelog/source/list');
  });

  it('should have a list of source names and buttons', function() {
    expect(rows.isPresent()).toBeTruthy();
    expect(rows.count()).toBe(3);
    expect(firstRowTds.count()).toBe(5);
    expect(bttnCreateSource.isPresent()).toBeTruthy();
    expect(bttnCreateSource.isDisplayed()).toBeTruthy();
    expect(bttnCreateSource.isEnabled()).toBeTruthy();
    expect(bttnClear.isPresent()).toBeTruthy();
    expect(bttnClear.isDisplayed()).toBeTruthy();
    expect(bttnClear.isEnabled()).toBeTruthy();
    expect(bttnRefresh.isPresent()).toBeTruthy();
    expect(bttnRefresh.isDisplayed()).toBeTruthy();
    expect(bttnRefresh.isEnabled()).toBeTruthy();
    expect(fltrName.isPresent()).toBeTruthy();
    expect(fltrName.isDisplayed()).toBeTruthy();
    expect(fltrName.isEnabled()).toBeTruthy();
    expect(fltrDescription.isPresent()).toBeTruthy();
    expect(fltrDescription.isDisplayed()).toBeTruthy();
    expect(fltrDescription.isEnabled()).toBeTruthy();
    expect(fltrUrl.isPresent()).toBeTruthy();
    expect(fltrUrl.isDisplayed()).toBeTruthy();
    expect(fltrUrl.isEnabled()).toBeTruthy();
    expect(fltrSourceName.isPresent()).toBeTruthy();
    expect(fltrSourceName.isDisplayed()).toBeTruthy();
    expect(fltrSourceName.isEnabled()).toBeTruthy();
  });

  it('should open a form when button \'Create Source\' is clicked',
    function() {
      bttnCreateSource.click();
      expect(browser.getCurrentUrl()).toBe(
        'http://localhost:8080/changelog/source/create');
    }
  );

  it('should open a view screen when button \'View\' is clicked', function() {
    bttnView.click();
    expect(browser.getCurrentUrl()).toBe(
      'http://localhost:8080/changelog/source/3');
  });

  describe('Filter:', function() {

    beforeEach(function() {
      bttnClear.click();
    });

    it('should clear the filter when button \'Clear\' is clicked', function() {
      fltrName.sendKeys('foo');
      fltrDescription.sendKeys('bar');
      fltrUrl.sendKeys('zoo');
      browser._selectDropdownbyNum(fltrSourceName, 3);
      expect(rows.count()).toBe(0);
      bttnClear.click();
      expect(rows.count()).toBe(3);
    });

    it('should refresh the list when button \'Refresh\' is clicked',
      function() {
        expect(rows.count()).toBe(3);
        bttnRefresh.click();
        expect(rows.count()).toBe(3);
      }
    );

    describe('Name:', function() {

      it('exact', function() {
        fltrName.sendKeys('Turbomaster website');
        expect(rows.count()).toBe(1);
      });

      it('partial, case-sensitive', function() {
        fltrName.sendKeys('master');
        expect(rows.count()).toBe(1);
      });

      it('partial, case-insensitive', function() {
        fltrName.sendKeys('mASTer');
        expect(rows.count()).toBe(1);
      });

    });

    describe('Description:', function() {

      it('partial, case-sensitive', function() {
        fltrDescription.sendKeys('CHRA');
        expect(rows.count()).toBe(1);
      });

      it('partial, case-insensitive', function() {
        fltrDescription.sendKeys('chRA');
        expect(rows.count()).toBe(1);
      });

    });

    describe('URL:', function() {

      it('partial, case-sensitive', function() {
        fltrUrl.sendKeys('turbomaster');
        expect(rows.count()).toBe(1);
      });

      it('partial, case-insensitive', function() {
        fltrUrl.sendKeys('TURbomaster');
        expect(rows.count()).toBe(1);
      });

    });

    describe('Source Name:', function() {

      it('partial, case-sensitive', function() {
        browser._selectDropdownbyNum(fltrSourceName, 5); // Invasion
        expect(rows.count()).toBe(2);
      });

    });

  });

  describe('View:', function() {

    var bttnChangelogSources, bttnEdit;

    beforeAll(function() {
      bttnChangelogSources = element(by.tiButton('Changelog Sources'));
      bttnEdit = element(by.tiButton('Edit'));
    });

    beforeEach(function() {
      browser.getCurrentUrl();
      browser.get('http://localhost:8080/changelog/source/3');
    });

    it('should has buttons', function() {
      expect(bttnChangelogSources.isPresent()).toBeTruthy();
      expect(bttnChangelogSources.isDisplayed()).toBeTruthy();
      expect(bttnChangelogSources.isEnabled()).toBeTruthy();
      expect(bttnEdit.isPresent()).toBeTruthy();
      expect(bttnEdit.isDisplayed()).toBeTruthy();
      expect(bttnEdit.isEnabled()).toBeTruthy();
      expect(bttnRemove.isPresent()).toBeTruthy();
      expect(bttnRemove.isDisplayed()).toBeTruthy();
      expect(bttnRemove.isEnabled()).toBeTruthy();
    });

    it('should open a list of changelog sources when button ' +
      '\'Changelog Sources\' is clicked',
      function() {
        bttnChangelogSources.click();
        expect(browser.getCurrentUrl()).toBe(
          'http://localhost:8080/changelog/source/list');
      }
    );

    it('should display a confirmation dialog when button \'Remove\' ' +
      'is clicked',
      function() {
        expect(dlg.isPresent()).toBeFalsy();
        bttnRemove.click();
        expect(dlg.isDisplayed()).toBeTruthy();
      }
    );

    it('should open a form to edit when button \'Edit\' is clicked',
      function() {
        bttnEdit.click();
        expect(browser.getCurrentUrl()).toBe(
          'http://localhost:8080/changelog/source/3/form');
      }
    );

  });

  describe('Edit:', function() {

    var bttnChangelogSources, bttnView,  bttnSave, bttnRevert,
      tabSource, tabAttachments, cntrlName, cntrlSourceName, cntrlDescription,
      cntrlUrl, cntrlFile, cntrlFileDesc, bttnUpload;

    beforeAll(function() {
      bttnChangelogSources = element(by.tiButton('Changelog Sources'));
      bttnView = element(by.tiButton('View'));
      bttnSave = element(by.tiButton('Save'));
      bttnRevert = element(by.tiButton('Revert'));
      var tabs = element(by.id('tabsSources')).all(by.tagName('a'));
      tabSource = tabs.first();
      tabAttachments = tabs.last();
      cntrlName = element(by.id('name'));
      cntrlSourceName = element(by.id('source_name'));
      cntrlDescription = element(by.id('desc'));
      cntrlUrl = element(by.id('url'));
      cntrlFile = element(by.name('file'));
      cntrlFileDesc = element(by.id('attach-descr'));
      bttnUpload = element(by.tiButton('Upload'));
    });

    beforeEach(function() {
      browser.getCurrentUrl();
      browser.get('http://localhost:8080/changelog/source/3/form');
    });

    it('should has navigation/edit buttons', function() {
      expect(bttnChangelogSources.isPresent()).toBeTruthy();
      expect(bttnChangelogSources.isDisplayed()).toBeTruthy();
      expect(bttnChangelogSources.isEnabled()).toBeTruthy();
      expect(bttnView.isPresent()).toBeTruthy();
      expect(bttnView.isDisplayed()).toBeTruthy();
      expect(bttnView.isEnabled()).toBeTruthy();
      expect(bttnSave.isPresent()).toBeTruthy();
      expect(bttnSave.isDisplayed()).toBeTruthy();
      expect(bttnSave.isEnabled()).toBeFalsy();
      expect(bttnRevert.isPresent()).toBeTruthy();
      expect(bttnRevert.isDisplayed()).toBeTruthy();
      expect(bttnRevert.isEnabled()).toBeFalsy();
      expect(tabSource.isPresent()).toBeTruthy();
      expect(tabAttachments.isPresent()).toBeTruthy();
    });

    it('should has UI controls and values to edit',
      function() {
        expect(cntrlName.isPresent()).toBeTruthy();
        expect(cntrlName.isDisplayed()).toBeTruthy();
        expect(cntrlName.isEnabled()).toBeTruthy();
        expect(cntrlSourceName.isPresent()).toBeTruthy();
        expect(cntrlSourceName.isDisplayed()).toBeTruthy();
        expect(cntrlSourceName.isEnabled()).toBeTruthy();
        expect(cntrlDescription.isPresent()).toBeTruthy();
        expect(cntrlDescription.isDisplayed()).toBeTruthy();
        expect(cntrlDescription.isEnabled()).toBeTruthy();
        expect(cntrlUrl.isPresent()).toBeTruthy();
        expect(cntrlUrl.isDisplayed()).toBeTruthy();
        expect(cntrlUrl.isEnabled()).toBeTruthy();
        expect(cntrlFile.isPresent()).toBeTruthy();
        expect(cntrlFile.isDisplayed()).toBeFalsy();
        expect(cntrlFileDesc.isPresent()).toBeTruthy();
        expect(cntrlFileDesc.isDisplayed()).toBeFalsy();
        expect(bttnUpload.isPresent()).toBeTruthy();
        expect(bttnUpload.isDisplayed()).toBeFalsy();
      }
    );

    it('should open a view with list of sources when ' +
      'button \'Changelog Sources\' is clicked',
      function() {
        bttnChangelogSources.click();
        expect(browser.getCurrentUrl()).toBe(
          'http://localhost:8080/changelog/source/list');
      }
    );

    it('should open a view with details when button \'View\' is clicked',
      function() {
        bttnView.click();
        expect(browser.getCurrentUrl()).toBe(
          'http://localhost:8080/changelog/source/3');
      }
    );

    it('should open tabs on clicks', function() {
      expect(cntrlName.isDisplayed()).toBeTruthy();
      expect(cntrlFileDesc.isDisplayed()).toBeFalsy();
      tabAttachments.click();
      browser.wait(EC.visibilityOf(cntrlFileDesc), 1000,
        'A tab \'Attachments\' has not been displayed after a click');
      expect(cntrlName.isDisplayed()).toBeFalsy();
      expect(cntrlFileDesc.isDisplayed()).toBeTruthy();
      tabSource.click();
      browser.wait(EC.visibilityOf(cntrlName), 1000,
        'A tab \'Source\' has not been displayed after a click');
      expect(cntrlName.isDisplayed()).toBeTruthy();
      expect(cntrlFileDesc.isDisplayed()).toBeFalsy();
    });

    describe('Buttons \'Save\' and \'Revert\' should be enabled when any ' +
      'value is chaned:',
      function() {

        it('Name', function() {
          expect(bttnSave.isEnabled()).toBeFalsy();
          expect(bttnRevert.isEnabled()).toBeFalsy();
          cntrlName.sendKeys('foo');
          expect(bttnSave.isEnabled()).toBeTruthy();
          expect(bttnRevert.isEnabled()).toBeTruthy();
        });

        it('Source name', function() {
          expect(bttnSave.isEnabled()).toBeFalsy();
          expect(bttnRevert.isEnabled()).toBeFalsy();
          browser._selectDropdownbyNum(cntrlSourceName, 1);
          expect(bttnSave.isEnabled()).toBeTruthy();
          expect(bttnRevert.isEnabled()).toBeTruthy();
        });

        it('Description', function() {
          expect(bttnSave.isEnabled()).toBeFalsy();
          expect(bttnRevert.isEnabled()).toBeFalsy();
          cntrlDescription.sendKeys('foo');
          expect(bttnSave.isEnabled()).toBeTruthy();
          expect(bttnRevert.isEnabled()).toBeTruthy();
        });

        it('URL', function() {
          expect(bttnSave.isEnabled()).toBeFalsy();
          expect(bttnRevert.isEnabled()).toBeFalsy();
          cntrlUrl.sendKeys('foo');
          expect(bttnSave.isEnabled()).toBeTruthy();
          expect(bttnRevert.isEnabled()).toBeTruthy();
        });

      }
    );

    it('should undo all changes when button \'Revert\' is clicked',
      function() {
        // Check buttons state before modifications.
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(bttnRevert.isEnabled()).toBeFalsy();
        // Do modifications.
        cntrlName.sendKeys('foo');
        browser._selectDropdownbyNum(cntrlSourceName, 1);
        cntrlDescription.sendKeys('foo');
        cntrlUrl.sendKeys('foo');
        // Check buttons state after modifications.
        expect(bttnSave.isEnabled()).toBeTruthy();
        expect(bttnRevert.isEnabled()).toBeTruthy();
        // Do undo.
        bttnRevert.click();
        // Make sure that undo reverts original values.
        expect(cntrlName.evaluate('data.crud.source.name'))
          .toBe('Invasion webpage');
        expect(cntrlDescription.evaluate('data.crud.source.description'))
          .toBeNull();
        expect(cntrlSourceName.evaluate('data.crud.source.sourceName'))
          .toEqual({ 'id': 6, 'name': 'Invasion' });
        expect(cntrlUrl.evaluate('data.crud.source.url'))
          .toBe('http://www.invasionautoproducts.com/20focogttu2.html');
        // Make sure that buttons are in expected state.
        expect(bttnSave.isEnabled()).toBeFalsy();
        expect(bttnRevert.isEnabled()).toBeFalsy();
      }
    );

    it('should upload attachment', function() {
      tabAttachments.click();
      browser.wait(EC.visibilityOf(cntrlFileDesc), 1000,
        'A tab \'Attachments\' has not been displayed after a click');
      expect(bttnSave.isEnabled()).toBeFalsy();
      expect(rows.count()).toBe(0); // rows count of attachments
      expect(bttnUpload.isEnabled()).toBeFalsy();
      expect(bttnRemove.isPresent()).toBeFalsy();
      // Upload an attachment but NOT save it.
      var attachment = path.resolve(__dirname, '../resources/washer.jpg');
      cntrlFile.sendKeys(attachment);
      cntrlFileDesc.sendKeys('foo');
      expect(bttnUpload.isEnabled()).toBeTruthy();
      bttnUpload.click();
      expect(bttnUpload.isEnabled()).toBeFalsy();
      expect(bttnSave.isEnabled()).toBeTruthy();
      expect(rows.count()).toBe(1);
      var tds = rows.first().all(by.tagName('td'));
      expect(tds.count()).toBe(3);
      expect(tds.first().getText()).toBe('washer.jpg');
      expect(tds.get(1).getText()).toBe('foo');
      expect(bttnRemove.isPresent()).toBeTruthy();
      expect(bttnRemove.isDisplayed()).toBeTruthy();
      // Remove just added attachment.
      bttnRemove.click();
      expect(dlg.isDisplayed()); // confirmation dialog
      var bttnOk = element(by.buttonText('Yes'));
      expect(bttnOk.isDisplayed()).toBeTruthy();
      bttnOk.click();
      expect(rows.count()).toBe(0);
      // Upload an attachment again and save it.
      var attachment2 = path.resolve(__dirname, '../resources/washer2.jpg');
      cntrlFile.sendKeys(attachment2);
      cntrlFileDesc.sendKeys('foo2');
      expect(bttnUpload.isEnabled()).toBeTruthy();
      bttnUpload.click();
      bttnSave.click();
      expect(browser.getCurrentUrl()).toBe(
        'http://localhost:8080/changelog/source/list');
      // Check that in details the attachment is displayed.
      browser.get('http://localhost:8080/changelog/source/3');
      expect(rows.count()).toBe(1);
      expect(tds.first().getText()).toBe('washer2.jpg');
      expect(tds.get(1).getText()).toBe('foo2');
      // Go to edit form and remove the attachment.
      browser.get('http://localhost:8080/changelog/source/3/form');
      tabAttachments.click();
      browser.wait(EC.visibilityOf(cntrlFileDesc), 1000,
        'A tab \'Attachments\' has not been displayed after a click');
      bttnRemove.click();
      expect(dlg.isDisplayed()); // confirmation dialog
      bttnOk.click();
      expect(rows.count()).toBe(0);
    });

  });

});
