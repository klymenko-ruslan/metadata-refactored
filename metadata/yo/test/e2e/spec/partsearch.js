'use strict';

describe('Part search:', function() {

  beforeAll(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/part/list');
  });

  it('should display all parts when no search criteria', function() {
    var rows = element.all(by.repeater('$part in $data'));
    expect(rows.count()).toBe(9);
  });

  describe('Search by purt number:', function() {

    var cntrlPartNumber;

    beforeEach(function() {
      cntrlPartNumber = element(by.id('fltrPartNumber'));
      cntrlPartNumber.clear();
    });

    it('should search by fully specified \'Part Number\'', function() {
      cntrlPartNumber.sendKeys('5303-101-5013');
      var rows = element.all(by.repeater('$part in $data'));
      expect(rows.count()).toBe(1);
      var tds = rows.first().all(by.tagName('td'));
      expect(tds.count()).toBe(9);
      expect(tds.get(0).getText()).toEqual('Compressor Cover');
      expect(tds.get(1).getText()).toEqual('KKK');
      expect(tds.get(2).getText()).toEqual('5303-101-5013');
      expect(tds.get(3).getText()).toEqual('');
    });

    it('should search by partially specified part number', function() {
      cntrlPartNumber.sendKeys('101');
      var rows = element.all(by.repeater('$part in $data'));
      expect(rows.count()).toBe(2);
      var p0 = rows.first().all(by.tagName('td')).get(2).getText().then(
        function(text) {
          return text;
        });
      var p1 = rows.last().all(by.tagName('td')).get(2).getText().then(
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

    var cntrlState;

    beforeEach(function() {
      cntrlState = element(by.id('fltrState'));
      browser._selectDropdownbyNum(cntrlState, 0);
    });

    it('should filter \'Active\' parts', function() {
      var rows = element.all(by.repeater('$part in $data'));
      expect(rows.count()).toBe(10);
      browser._selectDropdownbyNum(cntrlState, 1);
      rows = element.all(by.repeater('$part in $data'));
      expect(rows.count()).toBe(9);
    });

    it('should filter \'Inactive\' parts', function() {
      var rows = element.all(by.repeater('$part in $data'));
      expect(rows.count()).toBe(10);
      browser._selectDropdownbyNum(cntrlState, 2);
      rows = element.all(by.repeater('$part in $data'));
      expect(rows.count()).toBe(1);
    });

  });

});

