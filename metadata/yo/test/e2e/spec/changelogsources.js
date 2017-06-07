// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

fdescribe('Changelog Sources:', function() {

  var rows, firstRowTds, bttnView, bttnCreateSource, fltrName,
    fltrDescription, fltrUrl, fltrSourceName, bttnClear, bttnRefresh, dlg;

  beforeAll(function() {
    rows = element.all(by.repeater('rec in $data'));
    firstRowTds = rows.first().all(by.tagName('td'));
    var tdActions = firstRowTds.last();
    bttnCreateSource = element(by.tiButton('Create Source'));
    bttnView = tdActions.element(by.tiButton('View'));
    bttnClear = element(by.tiButton('Clear'));
    bttnRefresh = element(by.tiButton('Refresh'));
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

    var bttnChangelogSources, bttnEdit, bttnRemove;

    beforeAll(function() {
      bttnChangelogSources = element(by.tiButton('Changelog Sources'));
      bttnEdit = element(by.tiButton('Edit'));
      bttnRemove = element(by.tiButton('Remove'));
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

  fdescribe('Edit:', function() {

    var bttnChangelogSources, bttnView,  bttnSave, bttnRevert,
      tabSource, tabAttachments, cntrlName, cntrlSourceName, cntrlDescription,
      cntrlUrl, cntrlFile, cntrlFileDesc, bttnUpload;

    beforeAll(function() {
      bttnChangelogSources = element(by.tiButton('Changelog Sources'));
      bttnView = element(by.tiButton('View'));
      bttnSave = element(by.tiButton('Save'));
      bttnRevert = element(by.tiButton('Revert'));
      var tabs = element(by.id('tabSources')).all(by.tagName('a'));
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
        expect(bttnUpload.isPresent()).toBeTrue();
        expect(bttnUpload.isDisplayed()).toBeFalsy();
      }
    );

  });

});
