// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Users:', function() {

  beforeAll(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/security/users');
  });

  describe('List:', function() {

    var rows, fltrName, fltrUsername, fltrEmail, fltrEnabled, fltrAuthProvider,
      bttnClear;

    beforeAll(function() {
      rows = element.all(by.repeater('user in $data'));
      var filterRow = element(by.className('ng-table-filters'));
      var ths = filterRow.all(by.tagName('th'));
      fltrName = ths.first().element(by.tagName('input'));
      fltrUsername = ths.get(1).element(by.tagName('input'));
      fltrEmail = ths.get(2).element(by.tagName('input'));
      fltrEnabled = ths.get(3).element(by.tagName('select'));
      fltrAuthProvider = ths.get(4).element(by.tagName('select'));
      bttnClear = ths.last().element(by.tagName('button'));
    });

    beforeEach(function() {
      fltrName.clear();
      fltrUsername.clear();
      fltrEmail.clear();
      browser._selectReset(fltrEnabled);
      browser._selectReset(fltrAuthProvider);
    });

    it('should be displayed', function() {
      expect(browser.getCurrentUrl())
        .toBe('http://localhost:8080/security/users');
      expect(rows.isPresent()).toBeTruthy();
      expect(rows.count()).toBe(25);
    });

    it('should has a filter', function() {
      expect(fltrName.isPresent()).toBeTruthy();
      expect(fltrUsername.isPresent()).toBeTruthy();
      expect(fltrEmail.isPresent()).toBeTruthy();
      expect(fltrEnabled.isPresent()).toBeTruthy();
      expect(fltrAuthProvider.isPresent()).toBeTruthy();
    });

    it('should work button \'Clear\'', function() {
      expect(rows.count()).toBe(25);
      fltrName.sendKeys('foo');
      fltrUsername.sendKeys('foo');
      fltrEmail.sendKeys('foo');
      browser._selectDropdownbyNum(fltrEnabled, 3);
      browser._selectDropdownbyNum(fltrAuthProvider, 3);
      expect(rows.count()).toBe(0);
      bttnClear.click();
      expect(rows.count()).toBe(25);
    });

    describe('search by \'Name\':', function() {

      it('exactly (case sensitive)', function() {
        fltrName.sendKeys('Coby Reddick');
        expect(rows.count()).toBe(1);
      });

      it('exactly (case insensitive)', function() {
        fltrName.sendKeys('Coby Reddick');
        expect(rows.count()).toBe(1);
      });

      it('partly (case sensitive)', function() {
        expect(fltrName.isPresent()).toBeTruthy();
        fltrName.sendKeys('ck');
        expect(rows.count()).toBe(2);
      });

      it('partly (case insensitive)', function() {
        expect(fltrName.isPresent()).toBeTruthy();
        fltrName.sendKeys('CK');
        expect(rows.count()).toBe(2);
      });

    });

    describe('search by \'Username\':', function() {

      it('exactly (case sensitive)', function() {
        fltrUsername.sendKeys('creddick');
        expect(rows.count()).toBe(1);
      });

      it('exactly (case insensitive)', function() {
        fltrUsername.sendKeys('creDDick');
        expect(rows.count()).toBe(1);
      });

      it('partly (case sensitive)', function() {
        expect(fltrName.isPresent()).toBeTruthy();
        fltrUsername.sendKeys('bb');
        expect(rows.count()).toBe(1);
      });

      it('partly (case insensitive)', function() {
        expect(fltrName.isPresent()).toBeTruthy();
        fltrUsername.sendKeys('BB');
        expect(rows.count()).toBe(1);
      });

    });

  });

});
