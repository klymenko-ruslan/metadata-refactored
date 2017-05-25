'use strict';

describe('Part search:', function() {

  beforeAll(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/part/list');
  });

  it('should display all parts when no search criteria', function() {
    var rows = element.all(by.repeater('$part in $data'));
    expect(rows.count()).toBe(5);
  });

  describe('Search by purt number:', function() {
    var cntrlPartNumber;

    beforeEach(function() {
      cntrlPartNumber = element(by.id('fltrPartNumber'));
    });

    it('should search by fully specified part number', function() {
      cntrlPartNumber.sendKeys('5303-101-5013');
      var rows = element.all(by.repeater('$part in $data'));
      expect(rows.count()).toBe(1);
      //console.log(rows.first().all(by.tagName('td')).first().isPresent());
    });

  });

});

