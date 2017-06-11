// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

var EC = protractor.ExpectedConditions;
var _ = require('underscore');
var path = require('path');

describe('Sales notes:', function() {

  var rows, firstRowTds, fltrDraft, fltrSubmitted, fltrApproved, fltrRejected,
    fltrPublished, fltrPrimary, fltrRelated, fltrPrimaryPart, fltrComment,
    bttnViewNote, bttnRetract, bttnReject, bttnPublish, bttnSubmit,
    bttnApprove, dlg;

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
    var actionsTd = firstRowTds.last();
    // Buttons below are for a <TD> cell in the first row.
    bttnViewNote = actionsTd.element(by.partialLinkText('View Note'));
    bttnRetract = actionsTd.element(by.tiButton('Retract'));
    bttnReject = actionsTd.element(by.tiButton('Reject'));
    bttnPublish = actionsTd.element(by.tiButton('Publish'));
    bttnSubmit = actionsTd.element(by.tiButton('Submit'));
    bttnApprove = actionsTd.element(by.tiButton('Approve'));
    dlg = element(by.css('.modal-dialog'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/other/salesNotes');
  });

  it('should has a list of sales notes', function() {
    expect(rows.count()).toBe(7);
    expect(firstRowTds.count()).toBe(5);
  });

  it('should open a view to edit a sale note when button ' +
    '\'View Note\' is clicked',
    function() {
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

  describe('Buttons state in the \'Actions\' column are changed ' +
    'when they are clicked:',

    function() {

      beforeEach(function() {
        // In initial state only button 'rejected' is checked.
        // Code belove unchecks this button, so filter will have
        // no any checked buttons and all records are displayed.
        fltrRejected.click();
      });

      // Calculate how many sales notes are for each state.
      //
      // @return object like {"published":5,"approved":2,"rejected":2}
      function getCurrentStates() {
        var states = [];
        return rows.count().then(function(numRows) {
          for(var i = 0; i < numRows; i++) {
            rows.get(i).all(by.tagName('td')).get(3).getText().then(function(state) {
              states.push(state);
            });
          }
        }).then(function() {
          return _.countBy(states, function(state) { return state; });
        });
      }

      it('should has sales notes with expected states', function() {
        expect(getCurrentStates())
          .toEqual({ published: 5, approved: 2, rejected: 2 });
      });

      it('Buttons: >>Retract<< -> ( >>Publish<< , Reject) -> Retract, ' +
        'State: published -> approved -> published',
        function() {
          expect(bttnRetract.isPresent()).toBeTruthy();
          expect(bttnRetract.isDisplayed()).toBeTruthy();
          expect(bttnRetract.isEnabled()).toBeTruthy();
          expect(bttnPublish.isPresent()).toBeFalsy();
          expect(bttnReject.isPresent()).toBeFalsy();
          expect(bttnSubmit.isPresent()).toBeFalsy();
          expect(bttnApprove.isPresent()).toBeFalsy();
          expect(getCurrentStates())
            .toEqual({ published: 5, approved: 2, rejected: 2 });
          // Click a button 'Retract' and expect that:
          // 1. The button 'Retract' is disappeared.
          // 2. Buttons 'Publish' and 'Reject' are displayed.
          // 3. State is changed: published -> approved
          // 4. Number of the sales notes with status 'published'
          //    is decreased by 1 and number notes with status
          //    'approved' is increased by 1. Numbers for other
          //    states are not changed.
          bttnRetract.click();
          expect(bttnRetract.isPresent()).toBeFalsy();
          expect(bttnPublish.isPresent()).toBeTruthy();
          expect(bttnPublish.isDisplayed()).toBeTruthy();
          expect(bttnPublish.isEnabled()).toBeTruthy();
          expect(bttnReject.isPresent()).toBeTruthy();
          expect(bttnReject.isDisplayed()).toBeTruthy();
          expect(bttnReject.isEnabled()).toBeTruthy();
          expect(bttnSubmit.isPresent()).toBeFalsy();
          expect(bttnApprove.isPresent()).toBeFalsy();
          expect(getCurrentStates())
            .toEqual({ published: 5 - 1, approved: 2 + 1, rejected: 2 });
          // Click a button 'Publish' and check that state
          // returns to the initial state -- state at the beginning of the test.
          bttnPublish.click();
          expect(bttnRetract.isPresent()).toBeTruthy();
          expect(bttnRetract.isDisplayed()).toBeTruthy();
          expect(bttnRetract.isEnabled()).toBeTruthy();
          expect(bttnPublish.isPresent()).toBeFalsy();
          expect(bttnReject.isPresent()).toBeFalsy();
          expect(bttnSubmit.isPresent()).toBeFalsy();
          expect(bttnApprove.isPresent()).toBeFalsy();
          expect(getCurrentStates())
            .toEqual({ published: 5, approved: 2, rejected: 2 });
        }
      );

      it('Buttons: >>Retract<< -> ( Publish , >>Reject<<) -> >>Submit<< -> ' +
        '(>>Approve<<, Reject) -> (>>Publish<<, Reject) -> Retract, ' +
        'State: published -> approved -> rejected -> submitted -> ' +
        'approved -> published',
        function() {
          // Repeat test above but instead of clicking the button 'Publish'
          // we are clicking a button 'Reject'.
          expect(bttnRetract.isPresent()).toBeTruthy();
          expect(bttnRetract.isDisplayed()).toBeTruthy();
          expect(bttnRetract.isEnabled()).toBeTruthy();
          expect(bttnPublish.isPresent()).toBeFalsy();
          expect(bttnReject.isPresent()).toBeFalsy();
          expect(bttnSubmit.isPresent()).toBeFalsy();
          expect(bttnApprove.isPresent()).toBeFalsy();
          expect(getCurrentStates())
            .toEqual({ published: 5, approved: 2, rejected: 2 });
          bttnRetract.click();  // clicked 'Retract'
          bttnReject.click();   // clicked 'Reject'
          expect(bttnRetract.isPresent()).toBeFalsy();
          expect(bttnPublish.isPresent()).toBeFalsy();
          expect(bttnReject.isPresent()).toBeFalsy();
          expect(bttnSubmit.isPresent()).toBeTruthy();
          expect(bttnSubmit.isDisplayed()).toBeTruthy();
          expect(bttnSubmit.isEnabled()).toBeTruthy();
          expect(bttnApprove.isPresent()).toBeFalsy();
          expect(getCurrentStates())
            .toEqual({ published: 5 - 1, approved: 2, rejected: 2 + 1});
          bttnSubmit.click(); // clicked 'Submit'
          expect(bttnRetract.isPresent()).toBeFalsy();
          expect(bttnPublish.isPresent()).toBeFalsy();
          expect(bttnReject.isPresent()).toBeTruthy();
          expect(bttnReject.isDisplayed()).toBeTruthy();
          expect(bttnReject.isEnabled()).toBeTruthy();
          expect(bttnSubmit.isPresent()).toBeFalsy();
          expect(bttnApprove.isPresent()).toBeTruthy();
          expect(bttnApprove.isDisplayed()).toBeTruthy();
          expect(bttnApprove.isEnabled()).toBeTruthy();
          expect(getCurrentStates()).toEqual({ published: 5 - 1,
            approved: 2, rejected: 2, submitted: 1});
          bttnApprove.click(); // clicked 'Approve'
          expect(bttnRetract.isPresent()).toBeFalsy();
          expect(bttnPublish.isPresent()).toBeTruthy();
          expect(bttnPublish.isDisplayed()).toBeTruthy();
          expect(bttnPublish.isEnabled()).toBeTruthy();
          expect(bttnReject.isPresent()).toBeTruthy();
          expect(bttnReject.isDisplayed()).toBeTruthy();
          expect(bttnReject.isEnabled()).toBeTruthy();
          expect(bttnSubmit.isPresent()).toBeFalsy();
          expect(bttnApprove.isPresent()).toBeFalsy();
          expect(getCurrentStates())
            .toEqual({ published: 5 - 1, approved: 2 + 1, rejected: 2 });
          bttnPublish.click();  // click publish
          expect(bttnRetract.isPresent()).toBeTruthy();
          expect(bttnRetract.isDisplayed()).toBeTruthy();
          expect(bttnRetract.isEnabled()).toBeTruthy();
          expect(bttnPublish.isPresent()).toBeFalsy();
          expect(bttnReject.isPresent()).toBeFalsy();
          expect(bttnSubmit.isPresent()).toBeFalsy();
          expect(bttnApprove.isPresent()).toBeFalsy();
          expect(getCurrentStates())
            .toEqual({ published: 5, approved: 2, rejected: 2 });
        }
      );

    }
  );

  describe('View Note:', function() {

    var bttnViewPart, bttnSalesNotes, bttnEditNote, bttnRemove, bttnRetract,
      bttnReject, bttnPublish, bttnSubmit, bttnApprove, lblStatus, bttnUpload,
      bttnAddRelatedPart, bttnViewPrimary, inputFile,
      rowsAttachments, rowsRelatedParts, bttnSave, bttnCancel;

    beforeAll(function() {
      bttnViewPart = element(by.partialLinkText('View Part'));
      bttnSalesNotes = element(by.partialLinkText('Sales Notes'));
      bttnEditNote = element(by.partialLinkText('Edit Note'));
      bttnRemove = element(by.partialLinkText('Remove'));
      bttnRetract = element(by.partialLinkText('Retract'));
      bttnReject = element(by.partialLinkText('Reject'));
      bttnPublish = element(by.partialLinkText('Publish'));
      bttnSubmit = element(by.partialLinkText('Submit'));
      bttnApprove = element(by.partialLinkText('Approve'));
      lblStatus = element(by.id('salenote-state'));
      bttnUpload = element(by.tiButton('Upload'));
      bttnAddRelatedPart = element(by.partialLinkText('Add Related Part'));
      bttnViewPrimary = element(by.partialLinkText('View Primary'));
      inputFile = element(by.name('file'));
      rowsRelatedParts = element.all(by.repeater('relatedPart in $data'));
      rowsAttachments = element.all(by.repeater('attachment in $data'));
      bttnSave = element(by.partialLinkText('Save'));
      bttnCancel = element(by.partialLinkText('Cancel'));
    });

    beforeEach(function() {
      bttnViewNote.click();
    });

    it('shuld have a correct initial state', function() {
      expect(bttnViewPart.isPresent()).toBeTruthy();
      expect(bttnViewPart.isDisplayed()).toBeTruthy();
      expect(bttnViewPart.isEnabled()).toBeTruthy();
      expect(bttnSalesNotes.isPresent()).toBeTruthy();
      expect(bttnSalesNotes.isDisplayed()).toBeTruthy();
      expect(bttnSalesNotes.isEnabled()).toBeTruthy();
      expect(bttnEditNote.isPresent()).toBeTruthy();
      expect(bttnEditNote.isDisplayed()).toBeTruthy();
      expect(bttnEditNote.isEnabled()).toBeTruthy();
      expect(bttnRemove.isPresent()).toBeTruthy();
      expect(bttnRemove.isDisplayed()).toBeTruthy();
      expect(bttnRemove.isEnabled()).toBeTruthy();
      expect(bttnRetract.isPresent()).toBeTruthy();
      expect(bttnRetract.isDisplayed()).toBeTruthy();
      expect(bttnRetract.isEnabled()).toBeTruthy();
      expect(bttnReject.isPresent()).toBeFalsy();
      expect(bttnPublish.isPresent()).toBeFalsy();
      expect(bttnSubmit.isPresent()).toBeFalsy();
      expect(bttnApprove.isPresent()).toBeFalsy();
      expect(lblStatus.getText()).toBe('published');
      expect(inputFile.isPresent()).toBeTruthy();
      expect(inputFile.isDisplayed()).toBeTruthy();
      expect(inputFile.isEnabled()).toBeTruthy();
      expect(bttnUpload.isPresent()).toBeTruthy();
      expect(bttnUpload.isDisplayed()).toBeTruthy();
      expect(bttnUpload.isEnabled()).toBeFalsy();
      expect(bttnAddRelatedPart.isPresent()).toBeTruthy();
      expect(bttnAddRelatedPart.isDisplayed()).toBeTruthy();
      expect(bttnAddRelatedPart.isEnabled()).toBeTruthy();
      expect(bttnViewPart.isPresent()).toBeTruthy();
      expect(bttnViewPart.isDisplayed()).toBeTruthy();
      expect(bttnViewPart.isEnabled()).toBeTruthy();
      expect(rowsAttachments.count()).toBe(0);
      expect(rowsRelatedParts.count()).toBe(1);
    });

    it('should open part details view when a button \'View Part\' ' +
      'is clicked',
      function() {
        bttnViewPart.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/part/42119');
      }
    );

    it('should open sales note details view when a button ' +
      '\'Sales Notes ...\' is clicked',
      function() {
        bttnSalesNotes.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/part/42119/sales_notes');
      }
    );

    it('should open a confirmation dialog when a button \'Remove\' ' +
      'is clicked',
      function() {
        expect(dlg.isPresent()).toBeFalsy();
        bttnRemove.click();
        expect(dlg.isDisplayed()).toBeTruthy();
      }
    );

    describe('Changing of a status:', function() {

      it('published -> approved -> rejected -> submitted -> rejected -> ' +
        'submitted -> approved -> published', function() {
        expect(lblStatus.getText()).toBe('published');
        expect(bttnRetract.isPresent()).toBeTruthy();
        expect(bttnRetract.isDisplayed()).toBeTruthy();
        expect(bttnRetract.isEnabled()).toBeTruthy();
        expect(bttnReject.isPresent()).toBeFalsy();
        expect(bttnPublish.isPresent()).toBeFalsy();
        expect(bttnSubmit.isPresent()).toBeFalsy();
        expect(bttnApprove.isPresent()).toBeFalsy();
        bttnRetract.click();
        expect(lblStatus.getText()).toBe('approved');
        expect(bttnRetract.isPresent()).toBeFalsy();
        expect(bttnReject.isPresent()).toBeTruthy();
        expect(bttnReject.isDisplayed()).toBeTruthy();
        expect(bttnReject.isEnabled()).toBeTruthy();
        expect(bttnPublish.isPresent()).toBeTruthy();
        expect(bttnPublish.isDisplayed()).toBeTruthy();
        expect(bttnPublish.isEnabled()).toBeTruthy();
        expect(bttnSubmit.isPresent()).toBeFalsy();
        expect(bttnApprove.isPresent()).toBeFalsy();
        bttnReject.click();
        expect(lblStatus.getText()).toBe('rejected');
        expect(bttnSubmit.isPresent()).toBeTruthy();
        expect(bttnSubmit.isDisplayed()).toBeTruthy();
        expect(bttnSubmit.isEnabled()).toBeTruthy();
        expect(bttnReject.isPresent()).toBeFalsy();
        expect(bttnPublish.isPresent()).toBeFalsy();
        expect(bttnRetract.isPresent()).toBeFalsy();
        expect(bttnApprove.isPresent()).toBeFalsy();
        bttnSubmit.click();
        expect(lblStatus.getText()).toBe('submitted');
        expect(bttnSubmit.isPresent()).toBeFalsy();
        expect(bttnReject.isPresent()).toBeTruthy();
        expect(bttnReject.isDisplayed()).toBeTruthy();
        expect(bttnReject.isEnabled()).toBeTruthy();
        expect(bttnPublish.isPresent()).toBeFalsy();
        expect(bttnRetract.isPresent()).toBeFalsy();
        expect(bttnApprove.isPresent()).toBeTruthy();
        expect(bttnApprove.isDisplayed()).toBeTruthy();
        expect(bttnApprove.isEnabled()).toBeTruthy();
        bttnReject.click();
        expect(lblStatus.getText()).toBe('rejected');
        expect(bttnSubmit.isPresent()).toBeTruthy();
        expect(bttnSubmit.isDisplayed()).toBeTruthy();
        expect(bttnSubmit.isEnabled()).toBeTruthy();
        expect(bttnReject.isPresent()).toBeFalsy();
        expect(bttnPublish.isPresent()).toBeFalsy();
        expect(bttnRetract.isPresent()).toBeFalsy();
        expect(bttnApprove.isPresent()).toBeFalsy();
        bttnSubmit.click();
        expect(lblStatus.getText()).toBe('submitted');
        expect(bttnSubmit.isPresent()).toBeFalsy();
        expect(bttnReject.isPresent()).toBeTruthy();
        expect(bttnReject.isDisplayed()).toBeTruthy();
        expect(bttnReject.isEnabled()).toBeTruthy();
        expect(bttnPublish.isPresent()).toBeFalsy();
        expect(bttnRetract.isPresent()).toBeFalsy();
        expect(bttnApprove.isPresent()).toBeTruthy();
        expect(bttnApprove.isDisplayed()).toBeTruthy();
        expect(bttnApprove.isEnabled()).toBeTruthy();
        bttnApprove.click();
        expect(lblStatus.getText()).toBe('approved');
        expect(bttnRetract.isPresent()).toBeFalsy();
        expect(bttnReject.isPresent()).toBeTruthy();
        expect(bttnReject.isDisplayed()).toBeTruthy();
        expect(bttnReject.isEnabled()).toBeTruthy();
        expect(bttnPublish.isPresent()).toBeTruthy();
        expect(bttnPublish.isDisplayed()).toBeTruthy();
        expect(bttnPublish.isEnabled()).toBeTruthy();
        expect(bttnSubmit.isPresent()).toBeFalsy();
        expect(bttnApprove.isPresent()).toBeFalsy();
        bttnPublish.click();
        expect(lblStatus.getText()).toBe('published');
        expect(bttnRetract.isPresent()).toBeTruthy();
        expect(bttnRetract.isDisplayed()).toBeTruthy();
        expect(bttnRetract.isEnabled()).toBeTruthy();
        expect(bttnReject.isPresent()).toBeFalsy();
        expect(bttnPublish.isPresent()).toBeFalsy();
        expect(bttnSubmit.isPresent()).toBeFalsy();
        expect(bttnApprove.isPresent()).toBeFalsy();
      });
    });

    describe('Editing:', function() {

      var saleNoteComment, textareaComment;

      beforeAll(function() {
        saleNoteComment = element(by.binding('salesNote.comment'));
        textareaComment = element(by.model('editedSalesNote.comment'));
      });

      beforeEach(function() {
        bttnEditNote.click();
      });

      it('should rever all changes when button \'Cancel\' is pressed',
        function() {
          expect(bttnSave.isPresent()).toBeTruthy();
          expect(bttnSave.isDisplayed()).toBeTruthy();
          expect(bttnSave.isEnabled()).toBeTruthy();
          expect(bttnCancel.isPresent()).toBeTruthy();
          expect(bttnCancel.isDisplayed()).toBeTruthy();
          expect(bttnCancel.isEnabled()).toBeTruthy();
          textareaComment.sendKeys('foo');
          bttnCancel.click();
          expect(saleNoteComment.getText())
            .toBe('HT12 Nissan pick up application, 39.87 mm thrust bearing.');
        }
      );

    });

    describe('Attachments:', function() {

      it('should have expected initial state', function() {
        expect(rowsAttachments.count()).toBe(0);
        expect(bttnUpload.isPresent()).toBeTruthy();
        expect(bttnUpload.isDisplayed()).toBeTruthy();
        expect(bttnUpload.isEnabled()).toBeFalsy();
        expect(inputFile.isPresent()).toBeTruthy();
        expect(inputFile.isDisplayed()).toBeTruthy();
        expect(inputFile.isEnabled()).toBeTruthy();
      });

      it('should upload and remove an attachment', function() {
        var image2upload = path.resolve(__dirname, '../resources/washer.jpg');
        expect(bttnUpload.isEnabled()).toBeFalsy();
        inputFile.sendKeys(image2upload);
        browser.wait(EC.elementToBeClickable(bttnUpload), 5000,
          'The attachment was not uploaded in expected time period.');
        expect(bttnUpload.isEnabled()).toBeTruthy();
        bttnUpload.click();
        expect(rowsAttachments.count()).toBe(1);
        expect(bttnUpload.isEnabled()).toBeFalsy();
        var bttnRemoveAttachment = rowsAttachments.all(by.tiButton('Remove'))
          .first();
        expect(bttnRemoveAttachment.isPresent()).toBeTruthy();
        expect(bttnRemoveAttachment.isDisplayed()).toBeTruthy();
        expect(bttnRemoveAttachment.isEnabled()).toBeTruthy();
        bttnRemoveAttachment.click();
        expect(dlg.isDisplayed()).toBeTruthy(); // displayed a confirmation dialog
        var bttnYes = dlg.element(by.buttonText('Yes'));
        expect(bttnYes.isPresent()).toBeTruthy();
        expect(bttnYes.isDisplayed()).toBeTruthy();
        expect(bttnYes.isEnabled()).toBeTruthy();
        bttnYes.click(); // confirm
        browser.wait(EC.invisibilityOf(dlg), 3000, 'The confirmation dialog ' +
          'was not closed in an expected time period.');
        expect(rowsAttachments.count()).toBe(0);
      });

    });

    describe('Related Parts:', function() {

      it('should have an expected intial state', function() {
        expect(bttnAddRelatedPart.isPresent());
        expect(bttnAddRelatedPart.isDisplayed());
        expect(bttnAddRelatedPart.isEnabled());
        expect(rowsRelatedParts.count()).toBe(1);
        expect(bttnViewPrimary.isPresent());
        expect(bttnViewPrimary.isDisplayed());
        expect(bttnViewPrimary.isEnabled());
      });

      it('should display a view to add related part(s) when a button ' +
        '\'Add Related Part\' is clicked',
        function() {
          bttnAddRelatedPart.click();
          expect(browser.getCurrentUrl()).toBe('http://localhost:8080' +
            '/part/42119/sales_note/565/related_part/search');
        }
      );

      it('should display a view with a part details when a button ' +
        '\'View Primary\' is clicked',
        function() {
          bttnViewPrimary.click();
          expect(browser.getCurrentUrl())
            .toBe('http://localhost:8080/part/42119');
        }
      );

    });

  });

  describe('Add Related Part:', function() {

    var bttnViewPart, bttnViewSalesNote, bttnAddRelatedPart, bttnViewPrimary,
      rowsRelatedParts;

    beforeAll(function() {
      bttnViewPart = element(by.partialLinkText('View Part'));
      bttnViewSalesNote = element(by.partialLinkText('View Sales Note'));
      bttnAddRelatedPart = element(by.partialLinkText('Add Related Part'));
      bttnViewPrimary = element(by.partialLinkText('View Primary'));
      rowsRelatedParts = element.all(by.repeater('relatedPart in $data'));
    });

    beforeEach(function() {
      browser.get('http://localhost:8080/part/42119/sales_note/565/' +
        'related_part/search');
    });

    it('should have an expected initial state', function() {
      expect(bttnViewPart.isPresent()).toBeTruthy();
      expect(bttnViewPart.isDisplayed()).toBeTruthy();
      expect(bttnViewPart.isEnabled()).toBeTruthy();
      expect(bttnViewSalesNote.isPresent()).toBeTruthy();
      expect(bttnViewSalesNote.isDisplayed()).toBeTruthy();
      expect(bttnViewSalesNote.isEnabled()).toBeTruthy();
      expect(bttnAddRelatedPart.isPresent()).toBeTruthy();
      expect(bttnAddRelatedPart.isDisplayed()).toBeTruthy();
      //expect(bttnAddRelatedPart.isEnabled()).toBeFalsy();
      expect(rowsRelatedParts.count()).toBe(1);
      expect(bttnViewPrimary.isPresent()).toBeTruthy();
      expect(bttnViewPrimary.isDisplayed()).toBeTruthy();
      expect(bttnViewPrimary.isEnabled()).toBeTruthy();
    });

    it('should open a view with part details when a button \'View Part\' ' +
      'is clicked',
      function() {
        bttnViewPart.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/part/42119');
      }
    );

    it('should open a view with part details when a button \'View Primary\' ' +
      'is clicked',
      function() {
        bttnViewPrimary.click();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/part/42119');
      }
    );

    fit('should be possible to pick a part', function() {
      // A condition below means that message
      // 'Pick a part to add to the sales note.' is displayed.
      expect(bttnViewPart.evaluate('pickedPart')).toBeFalsy();
      var bttnPick = element.all(by.partialLinkText('Pick')).first();
      bttnPick.click(); // pick any part
      // Make sure that message 'Pick a part to add to the sales note.'
      // is replaced by the picked part.
      expect(bttnViewPart.evaluate('pickedPart')).toBeTruthy();
      expect(bttnAddRelatedPart.isEnabled()).toBeTruthy();
    });

  });

});
