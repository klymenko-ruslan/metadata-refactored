// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

fdescribe('Sales notes:', function() {

  var rows, firstRowTds, fltrDraft, fltrSubmitted, fltrApproved, fltrRejected,
    fltrPublished, fltrPrimary, fltrRelated, fltrPrimaryPart, fltrComment;

  beforeAll(function() {
    rows = element.all(by.repeater('row in $data'));
    firstRowTds = rows.first().all(by.tagName('td'));
    var fltrGrpStates = element(by.id('fltrStates')).all(by.tagName('label'));
    fltrDraft = fltrGrpStates.get(0);
    fltrSubmitted = fltrGrpStates.get(1);
    fltrApproved = fltrGrpStates.get(2);
    fltrRejected = fltrGrpStates.get(3);
    fltrPublished = fltrGrpStates.get(4);
    var fltrGrpPrimary = element(by.id('fltrPrimary'))
      .all(by.tagName('label'));
    fltrPrimary = fltrGrpPrimary.first();
    fltrRelated = fltrGrpPrimary.last();
    var fltrGrpOther = element(by.id('fltrOther')).all(by.tagName('input'));
    fltrPrimaryPart = fltrGrpOther.first();
    fltrComment = fltrGrpOther.last();
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/other/salesNotes');
  });

  it('should has a list of sales notes', function() {
    expect(rows.count()).toBe(7);
    expect(firstRowTds.count()).toBe(5);
  });

  fit('should open a view to edit a sale note when button ' +
    '\'View Note\' is clicked',
    function() {
      var bttnViewNote = firstRowTds.last().element(by.tiButton('View Note'));
      expect(bttnViewNote.isPresent()).toBeTruthy();
      expect(bttnViewNote.isDisplayed()).toBeTruthy();
      expect(bttnViewNote.isEnabled()).toBeTruthy();
      bttnViewNote.click();
      expect(browser.getCurrentUrl())
        .toBe('http://localhost:8080/part/42119/sales_note/565');
    }
  );

  describe('Filter:', function() {

    it('should has UI controls to filer records', function() {
      expect(fltrDraft.isPresent()).toBeTruthy();
      expect(fltrDraft.isDisplayed()).toBeTruthy();
      expect(fltrSubmitted.isPresent()).toBeTruthy();
      expect(fltrSubmitted.isDisplayed()).toBeTruthy();
      expect(fltrApproved.isPresent()).toBeTruthy();
      expect(fltrApproved.isDisplayed()).toBeTruthy();
      expect(fltrRejected.isPresent()).toBeTruthy();
      expect(fltrRejected.isDisplayed()).toBeTruthy();
      expect(fltrPublished.isPresent()).toBeTruthy();
      expect(fltrPublished.isDisplayed()).toBeTruthy();
      expect(fltrPrimary.isPresent()).toBeTruthy();
      expect(fltrPrimary.isDisplayed()).toBeTruthy();
      expect(fltrRelated.isPresent()).toBeTruthy();
      expect(fltrRelated.isDisplayed()).toBeTruthy();
      expect(fltrComment.isPresent()).toBeTruthy();
      expect(fltrComment.isDisplayed()).toBeTruthy();
      expect(fltrComment.isEnabled()).toBeTruthy();
    });

    it('should filter by \'draft\'', function() {
      expect(rows.count()).toBe(7);
      fltrDraft.click();
      expect(rows.count()).toBe(7);
    });

    it('should filter by \'submitted\'', function() {
      expect(rows.count()).toBe(7);
      fltrSubmitted.click();
      expect(rows.count()).toBe(7);
    });

    it('should filter by \'approved\'', function() {
      expect(rows.count()).toBe(7);
      fltrApproved.click();
      expect(rows.count()).toBe(5);
    });

    it('should filter by \'rejected\'', function() {
      expect(rows.count()).toBe(7);
      fltrRejected.click();
      expect(rows.count()).toBe(9);
    });

    it('should filter by \'published\'', function() {
      expect(rows.count()).toBe(7);
      fltrPublished.click();
      expect(rows.count()).toBe(2);
    });

    it('should filter by \'Primary\'', function() {
      expect(rows.count()).toBe(7);
      fltrPrimary.click();
      expect(rows.count()).toBe(0);
    });

    it('should filter by \'Related\'', function() {
      expect(rows.count()).toBe(7);
      fltrRelated.click();
      expect(rows.count()).toBe(7);
    });

    describe('\'Primary Part\':', function() {

      it('exactly', function() {
        expect(rows.count()).toBe(7);
        fltrPrimaryPart.sendKeys('7-G-1657');
        expect(rows.count()).toBe(2);
      });

      it('partly, case-sensitive', function() {
        expect(rows.count()).toBe(7);
        fltrPrimaryPart.sendKeys('G-06');
        expect(rows.count()).toBe(2);
      });

      it('partly, case-insensitive', function() {
        expect(rows.count()).toBe(7);
        fltrPrimaryPart.sendKeys('g-06');
        expect(rows.count()).toBe(2);
      });

    });

    describe('\'Comment\':', function() {

      it('partly, case-sensitive', function() {
        expect(rows.count()).toBe(7);
        fltrComment.sendKeys('seal');
        expect(rows.count()).toBe(5);
      });

      it('partly, case-insensitive', function() {
        expect(rows.count()).toBe(7);
        fltrComment.sendKeys('SEAL');
        expect(rows.count()).toBe(5);
      });

    });

  });

});
