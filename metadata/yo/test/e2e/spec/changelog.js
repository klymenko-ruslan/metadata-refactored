// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Changelog:', function() {

  var rows, firstRowTds, fltrStartDate, fltrFinishDate, fltrUser, fltrService,
    fltrDescription, fltrData, bttnApply;

  beforeAll(function() {
    rows = element.all(by.repeater('rec in $data'));
    firstRowTds = rows.first().all(by.tagName('td'));
    fltrStartDate = element(by.model('search.startDate'));
    fltrFinishDate = element(by.model('search.finishDate'));
    fltrUser = element(by.model('search.user'));
    fltrService = element(by.model('search.service'));
    fltrDescription = element(by.model('search.description'));
    fltrData = element(by.model('search.data'));
    bttnApply = element(by.tiButton('Apply'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/changelog/list');
    // Clear filter.
    fltrStartDate.clear();
    fltrFinishDate.clear();
    browser._selectReset(fltrUser);
    browser._selectReset(fltrService);
    fltrDescription.clear();
    fltrData.clear();
    bttnApply.click();
  });

  it('should has changelog rows', function() {
    expect(rows.count()).toBe(25);
    expect(firstRowTds.count()).toBe(5)
  });

  it('when button \'View\' is clicked it should open a dialog with details',
    function() {
      var bttnView = firstRowTds.get(4).element(by.tiButton('View'));
      expect(bttnView.isPresent()).toBeTruthy();
      expect(bttnView.isDisplayed()).toBeTruthy();
      var dlg = element(by.css('.modal'));
      expect(dlg.isPresent()).toBeFalsy();
      bttnView.click();
      expect(dlg.isPresent()).toBeTruthy();
      expect(dlg.isDisplayed()).toBeTruthy();
    }
  );

  describe('Filter:', function() {

    it('should have UI controls to filter', function() {
      expect(fltrStartDate.isPresent()).toBeTruthy();
      expect(fltrStartDate.isDisplayed()).toBeTruthy();
      expect(fltrFinishDate.isPresent()).toBeTruthy();
      expect(fltrFinishDate.isDisplayed()).toBeTruthy();
      expect(fltrUser.isPresent()).toBeTruthy();
      expect(fltrUser.isDisplayed()).toBeTruthy();
      expect(fltrService.isPresent()).toBeTruthy();
      expect(fltrService.isDisplayed()).toBeTruthy();
      expect(fltrDescription.isPresent()).toBeTruthy();
      expect(fltrDescription.isDisplayed()).toBeTruthy();
      expect(fltrData.isPresent()).toBeTruthy();
      expect(fltrData.isDisplayed()).toBeTruthy();
      expect(bttnApply.isPresent()).toBeTruthy();
      expect(bttnApply.isDisplayed()).toBeTruthy();
    });

    xit('should filter by start date only', function() {
      // We can't execute this test repeatedly because other
      // tests have impact on the changelog.
      expect(rows.count()).toBe(25);
      fltrStartDate.sendKeys('2014-12-12');
      bttnApply.click();
      expect(rows.count()).toBe(2);
    });

    it('should filter by finish date only', function() {
      expect(rows.count()).toBe(25);
      fltrFinishDate.sendKeys('2014-10-10');
      bttnApply.click();
      expect(rows.count()).toBe(5);
    });

    it('should filter by a time period (start date <= finish date)',
      function() {
        expect(rows.count()).toBe(25);
        fltrStartDate.sendKeys('2014-10-10');
        fltrFinishDate.sendKeys('2014-10-11');
        bttnApply.click();
        expect(rows.count()).toBe(9);
      }
    );

    it('should filter by a time period (start date >= finish date)',
      function() {
        expect(rows.count()).toBe(25);
        fltrStartDate.sendKeys('2014-10-11');
        fltrFinishDate.sendKeys('2014-10-10');
        bttnApply.click();
        expect(rows.count()).toBe(9);
      }
    );

    it('should filter by \'User\'', function() {
      expect(rows.count()).toBe(25);
      browser._selectDropdownbyNum(fltrUser, 3); // Brian Malewicz
      bttnApply.click();
      expect(rows.count()).toBe(1);
    });

    it('should filter by \'Description\'', function() {
      expect(rows.count()).toBe(25);
      fltrDescription.sendKeys('created interchange');
      bttnApply.click();
      expect(rows.count()).toBe(9);
    });

    it('should filter by \'Data\'', function() {
      expect(rows.count()).toBe(25);
      fltrData.sendKeys('parts');
      bttnApply.click();
      expect(rows.count()).toBe(9);
    });

    it('should apply filter on pressed Enter key', function() {
      expect(rows.count()).toBe(25);
      fltrData.sendKeys('parts');
      browser._pressEnter();
      expect(rows.count()).toBe(9);
    });

  });

});
