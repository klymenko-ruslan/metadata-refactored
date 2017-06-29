// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Part search:', function() {

  var fltrPartNumber, fltrState, fltrManufacturer, fltrName,
    fltrPartType, cntrlShowCritDims;
  var rows, firstRowTds, lastRowTds;

  beforeAll(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/part/list');
    fltrPartNumber = element(by.id('fltrPartNumber'));
    fltrState = element(by.id('fltrState'));
    fltrManufacturer = element(by.id('fltrManufacturer'));
    fltrName = element(by.id('fltrName'));
    cntrlShowCritDims = element(by.model('showCriticalDimensions'));
    fltrPartType = element(by.id('fltrPartType'));
    rows = element.all(by.repeater('$part in $data'));
    firstRowTds = rows.first().all(by.tagName('td'));
    lastRowTds = rows.last().all(by.tagName('td'));
  });

  beforeEach(function() {
    fltrPartNumber.clear();
    fltrName.clear();
    browser._selectDropdownbyNum(fltrState, 0);
    browser._selectDropdownbyNum(fltrManufacturer, 0);
    browser._selectDropdownbyNum(fltrPartType, 0);
    // Reset checkbox 'show critical dimensions' if any.
    if(cntrlShowCritDims.isSelected()) {
      cntrlShowCritDims.click();
    }
  });

  it('should display all parts when no search criteria', function() {
    var rows = element.all(by.repeater('$part in $data'));
    expect(rows.count()).toBe(10);
  });

  describe('Search by part number:', function() {

    it('should have UI control to filter by a part number', function() {
      expect(fltrPartType.isPresent()).toBeTruthy();
      expect(fltrPartType.isDisplayed()).toBeTruthy();
    });

    it('should search by fully specified \'Part Number\'', function() {
      fltrPartNumber.sendKeys('5303-101-5013');
      expect(rows.count()).toBe(1);
      expect(firstRowTds.count()).toBe(5);
      expect(firstRowTds.get(0).getText()).toEqual('Compressor Cover');
      expect(firstRowTds.get(1).getText()).toEqual('KKK');
      expect(firstRowTds.get(2).getText()).toEqual('5303-101-5013');
      expect(firstRowTds.get(3).getText()).toEqual('');
    });

    it('should search by partially specified part number', function() {
      fltrPartNumber.sendKeys('101');
      expect(rows.count()).toBe(2);
      var p0 = firstRowTds.get(2).getText().then(
        function(text) {
          return text;
        });
      var p1 = lastRowTds.get(2).getText().then(
        function(text) {
          return text;
        });
      protractor.promise.all([p0, p1]).then(function(result) {
        expect(result.length).toBe(2);
        expect(result).toContain('5303-101-5013');
        expect(result).toContain('5304-101-5003');
      });
    });

  });

  describe('Search by \'State\':', function() {

    it('should have UI control to filter by a state', function() {
      expect(fltrState.isPresent()).toBeTruthy();
      expect(fltrState.isDisplayed()).toBeTruthy();
    });

    it('should filter \'Active\' parts', function() {
      expect(rows.count()).toBe(10);
      browser._selectDropdownbyNum(fltrState, 1);
      expect(rows.count()).toBe(10);
    });

    it('should filter \'Inactive\' parts', function() {
      expect(rows.count()).toBe(10);
      browser._selectDropdownbyNum(fltrState, 2);
      expect(rows.count()).toBe(2);
    });

  });

  describe('Search by \'Manufacturer\':', function() {

    it('should have UI control to filter by a manufacturer', function() {
      expect(fltrManufacturer.isPresent()).toBeTruthy();
      expect(fltrManufacturer.isDisplayed()).toBeTruthy();
    });

    it('Garret', function() {
      expect(rows.count()).toBe(10);
      browser._selectDropdownbyNum(fltrManufacturer, 1); // Garret
      expect(rows.count()).toBe(3);
    });

    it('Holset', function() {
      expect(rows.count()).toBe(10);
      browser._selectDropdownbyNum(fltrManufacturer, 2); // Holset
      expect(rows.count()).toBe(3);
    });

  });

  describe('Search by \'Name\':', function() {

    it('should have UI control to filter by a name', function() {
      expect(fltrName.isPresent()).toBeTruthy();
      expect(fltrName.isDisplayed()).toBeTruthy();
    });

    it('should search by text \'plate\'', function() {
      expect(rows.count()).toBe(10);
      fltrName.sendKeys('plate');
      expect(rows.count()).toBe(3);
      var promises = [];
      for(var i = 0; i < 3; i++) {
        var p = rows.get(i).all(by.tagName('td')).get(3).getText().then(
          function(text) {
            return text;
          });
        promises.push(p);
      }
      expect(promises.length).toBe(3);
      protractor.promise.all(promises).then(function(result) {
        expect(result.length).toBe(3);
        expect(result).toContain('Backplate (No longer available)');
        expect(result).toContain('THRUST PLATE, T18');
      });
    });

  });

  describe('Search by \'Part Type\':', function() {

    it('should have UI control to filter by a part type', function() {
      expect(fltrPartType.isPresent()).toBeTruthy();
      expect(fltrPartType.isDisplayed()).toBeTruthy();
    });

    it('Actuator', function() {
      expect(rows.count()).toBe(10);
      browser._selectDropdownbyNum(fltrPartType, 1);
      expect(rows.count()).toBe(0);
    });

    it('Backplate', function() {
      expect(rows.count()).toBe(10);
      browser._selectDropdownbyNum(fltrPartType, 2);
      expect(rows.count()).toBe(3);
    });

 });

  describe('Table with list of parts', function() {

    it('should not allow to check \'Show critical dimensions\' checkbox ' +
       'when part type is not selected', function () {
      expect(cntrlShowCritDims.isPresent()).toBeTruthy();
      expect(cntrlShowCritDims.isEnabled()).toBeFalsy();
    });

    it('should not allow to check \'Show critical dimensions\' checkbox ' +
       'when selected part type has no critical dimensions', function () {
      browser._selectDropdownbyNum(fltrPartType, 1); // actuator
      expect(cntrlShowCritDims.isEnabled()).toBeFalsy();
    });

    it('should allow to check \'Show critical dimensions\' checkbox ' +
       'when selected part type has critical dimensions', function () {
      browser._selectDropdownbyNum(fltrPartType, 2); // backplate
      expect(cntrlShowCritDims.isEnabled()).toBeTruthy();
    });

    it('should allow to display critical dimensions in the table',
      function () {
        browser._selectDropdownbyNum(fltrPartType, 2); // backplate
        expect(cntrlShowCritDims.isEnabled()).toBeTruthy();
        expect(cntrlShowCritDims.isSelected()).toBeFalsy();
        expect(firstRowTds.count()).toBe(5);
        cntrlShowCritDims.click();
        expect(cntrlShowCritDims.isSelected()).toBeTruthy();
        expect(firstRowTds.count()).toBe(33);
      }
    );

  });

});
