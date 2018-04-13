// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

var path = require('path');
var EC = protractor.ExpectedConditions;

fdescribe('Part details:', function() {

  var bttnCreateXRef, bttnWhereUsed, bttnInterchanges, bttnSalesNotes,
    lblStateActive, lblStateInactive, bttnReindex,
    tabDetails, tabDimensions, tabTurboTypes, tabKits, tabApplications,
    tabTurbos, tabNonStandard, tabAuditLog, tabPrices, tabAlsoBought,
    dlgCreateXRef;

  beforeAll(function() {
    bttnCreateXRef = element(by.tiButton('Create X Ref'));
    bttnWhereUsed = element(by.partialLinkText('Where used'));
    bttnInterchanges = element(by.partialLinkText('Interchanges'));
    bttnSalesNotes = element(by.partialLinkText('Sales Notes'));
    lblStateActive = $('.nav-pills').$$('li').last().$$('strong').first();
    lblStateInactive = $('.nav-pills').$$('li').last().$$('strong').last();
    bttnReindex = element(by.tiButton('Reindex'));
    tabDetails = element(by.id('tab-details'));
    tabDimensions = element(by.id('tab-critical-dimensions'));
    tabTurboTypes = element(by.id('tab-turbo-types'));
    tabKits = element(by.id('tab-kits'));
    tabApplications = element(by.id('tab-applications'));
    tabTurbos = element(by.id('tab-turbos'));
    tabNonStandard = element(by.id('tab-nonstandard'));
    tabAuditLog = element(by.id('tab-audit-log'));
    tabPrices = element(by.id('tab-prices'));
    tabAlsoBought = element(by.id('tab-also-bought'));
    dlgCreateXRef = element(by.id('dlg-create-x-ref-title'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/part/64956');
  });

  it('should have an initial state', function() {
    expect(bttnCreateXRef.isPresent()).toBeTruthy();
    expect(bttnCreateXRef.isDisplayed()).toBeTruthy();
    expect(bttnCreateXRef.isEnabled()).toBeTruthy();
    expect(bttnWhereUsed.isPresent()).toBeTruthy();
    expect(bttnWhereUsed.isDisplayed()).toBeTruthy();
    expect(bttnWhereUsed.isEnabled()).toBeTruthy();
    expect(bttnInterchanges.isPresent()).toBeTruthy();
    expect(bttnInterchanges.isDisplayed()).toBeTruthy();
    expect(bttnInterchanges.isEnabled()).toBeTruthy();
    expect(bttnSalesNotes.isPresent()).toBeTruthy();
    expect(bttnSalesNotes.isDisplayed()).toBeTruthy();
    expect(bttnSalesNotes.isEnabled()).toBeTruthy();
    expect(lblStateActive.isPresent()).toBeTruthy();
    expect(lblStateActive.isDisplayed()).toBeTruthy();
    expect(lblStateInactive.isPresent()).toBeTruthy();
    expect(lblStateInactive.isDisplayed()).toBeFalsy();
    expect(bttnReindex.isPresent()).toBeTruthy();
    expect(bttnReindex.isDisplayed()).toBeTruthy();
    expect(bttnReindex.isEnabled()).toBeTruthy();
    expect(tabDetails.isPresent()).toBeTruthy();
    expect(tabDetails.isDisplayed()).toBeTruthy();
    expect(tabDetails.isEnabled()).toBeTruthy();
    expect(tabDimensions.isPresent()).toBeTruthy();
    expect(tabDimensions.isDisplayed()).toBeTruthy();
    expect(tabDimensions.isEnabled()).toBeTruthy();
    expect(tabTurboTypes.isPresent()).toBeTruthy();
    expect(tabTurboTypes.isDisplayed()).toBeTruthy();
    expect(tabTurboTypes.isEnabled()).toBeTruthy();
    expect(tabKits.isPresent()).toBeTruthy();
    expect(tabKits.isDisplayed()).toBeFalsy();
    expect(tabKits.isEnabled()).toBeTruthy();
    expect(tabApplications.isPresent()).toBeTruthy();
    expect(tabApplications.isDisplayed()).toBeFalsy();
    expect(tabTurbos.isPresent()).toBeTruthy();
    expect(tabTurbos.isDisplayed()).toBeFalsy();
    expect(tabNonStandard.isPresent()).toBeTruthy();
    expect(tabNonStandard.isDisplayed()).toBeFalsy();
    expect(tabAuditLog.isPresent()).toBeTruthy();
    expect(tabAuditLog.isDisplayed()).toBeTruthy();
    expect(tabAuditLog.isEnabled()).toBeTruthy();
    expect(tabPrices.isPresent()).toBeTruthy();
    expect(tabPrices.isDisplayed()).toBeTruthy();
    expect(tabPrices.isEnabled()).toBeTruthy();
    expect(tabAlsoBought.isPresent()).toBeTruthy();
    expect(tabAlsoBought.isDisplayed()).toBeTruthy();
    expect(tabAlsoBought.isEnabled()).toBeTruthy();
  });

  it('should be possible to press button \'Create X Ref\'', function() {
    expect(dlgCreateXRef.isPresent()).toBeFalsy();
    bttnCreateXRef.click();
    expect(dlgCreateXRef.isDisplayed()).toBeTruthy();
  });

  it('should be possible to press button \'Where used ...\'', function() {
    bttnWhereUsed.click();
    expect(browser.getCurrentUrl())
      .toBe('http://localhost:8080/part/64956/ancestors');
  });

  it('should be possible to press button \'Interchanges ...\'', function() {
    bttnInterchanges.click();
    expect(browser.getCurrentUrl())
      .toBe('http://localhost:8080/part/64956/interchange/search');
  });

  it('should be possible to press button \'Sales Notes ...', function() {
    bttnSalesNotes.click();
    expect(browser.getCurrentUrl())
      .toBe('http://localhost:8080/part/64956/sales_notes');
  });

  it('shoul be possible to press button \'Reindex\'', function() {
    var bttn = bttnReindex;
    expect(bttn.isPresent()).toBeTruthy();
    expect(bttn.isDisplayed()).toBeTruthy();
    expect(bttn.isEnabled()).toBeTruthy();
    expect(browser.getCurrentUrl()).toBe('http://localhost:8080/part/64956');
    bttnReindex.click();
    expect(bttn.isPresent()).toBeTruthy();
    expect(bttn.isDisplayed()).toBeTruthy();
    expect(bttn.isEnabled()).toBeTruthy();
    expect(browser.getCurrentUrl()).toBe('http://localhost:8080/part/64956');
  });

  describe('Tab - Part Details:', function() {

    describe('Edit:', function() {
      // TODO
    });

    describe('CRUD images:', function() {

      var bttnAddImage, elmDisplayPerPage, ulImagesList, imageListPaginator,
        noPartImages;

      beforeAll(function() {
        elmDisplayPerPage = element(by.id('img-pg-sz'));
        bttnAddImage = element(by.tiButton('Add Image'));
        ulImagesList = element(by.id('part-images-list'));
        imageListPaginator = element(by.id('part-images')).all(by.tagName('ul'))
          .last();
        noPartImages = element(by.id('no-part-images'));
      });

      it('should have an initial state', function() {
        expect(elmDisplayPerPage.isPresent()).toBeTruthy();
        expect(elmDisplayPerPage.isDisplayed()).toBeTruthy();
        expect(elmDisplayPerPage.isEnabled()).toBeTruthy();
        expect(bttnAddImage.isPresent()).toBeTruthy();
        expect(bttnAddImage.isDisplayed()).toBeTruthy();
        expect(bttnAddImage.isEnabled()).toBeTruthy();
        expect(ulImagesList.isPresent()).toBeTruthy();
        expect(noPartImages.isPresent()).toBeTruthy();
        expect(noPartImages.isDisplayed()).toBeFalsy();
      });

      it('should change number of displayed images', function() {
        browser._selectDropdownbyNum(elmDisplayPerPage, 0);
        expect(ulImagesList.all(by.tagName('li')).count()).toBe(1);
        expect(bttnAddImage.isPresent()).toBeTruthy();
        expect(bttnAddImage.isDisplayed()).toBeTruthy();
        expect(imageListPaginator.isDisplayed()).toBeTruthy();
        browser._selectDropdownbyNum(elmDisplayPerPage, 1);
        expect(ulImagesList.all(by.tagName('li')).count()).toBe(2);
        expect(bttnAddImage.isPresent()).toBeTruthy();
        expect(bttnAddImage.isDisplayed()).toBeTruthy();
        expect(imageListPaginator.isDisplayed()).toBeFalsy();
        browser._selectDropdownbyNum(elmDisplayPerPage, 2);
        expect(ulImagesList.all(by.tagName('li')).count()).toBe(2);
        expect(bttnAddImage.isPresent()).toBeTruthy();
        expect(bttnAddImage.isDisplayed()).toBeTruthy();
        expect(imageListPaginator.isDisplayed()).toBeFalsy();
        browser._selectDropdownbyNum(elmDisplayPerPage, 3);
        expect(ulImagesList.all(by.tagName('li')).count()).toBe(2);
        expect(bttnAddImage.isPresent()).toBeTruthy();
        expect(bttnAddImage.isDisplayed()).toBeTruthy();
        expect(imageListPaginator.isDisplayed()).toBeFalsy();
        browser._selectDropdownbyNum(elmDisplayPerPage, 4); // all images
        expect(ulImagesList.all(by.tagName('li')).count()).toBe(2);
        expect(bttnAddImage.isPresent()).toBeTruthy();
        expect(bttnAddImage.isDisplayed()).toBeTruthy();
      });

      it('should display image correctly', function() {
        browser._selectDropdownbyNum(elmDisplayPerPage, 0);
        var form = ulImagesList.all(by.tagName('li')).first()
          .element(by.tagName('form'));
        var bttnDelete = form.element(by.tagName('button'));
        expect(bttnDelete.isPresent()).toBeTruthy();
        expect(bttnDelete.isDisplayed()).toBeTruthy();
        expect(bttnDelete.isEnabled()).toBeTruthy();
        var bttnPrim = form.all(by.tagName('input')).first();
        expect(bttnPrim.isPresent()).toBeTruthy();
        expect(bttnPrim.isDisplayed()).toBeTruthy();
        expect(bttnPrim.isEnabled()).toBeTruthy();
        expect(bttnPrim.evaluate('image.main')).toBe(false);
        var bttnPub = form.all(by.tagName('input')).last();
        expect(bttnPub.isPresent()).toBeTruthy();
        expect(bttnPub.isDisplayed()).toBeTruthy();
        expect(bttnPub.isEnabled()).toBeTruthy();
        expect(bttnPub.evaluate('image.publish')).toBe(true);
      });


      describe('Upload image:', function() {

        var inptFile, bttnPublish, bttnUnpublish, bttnCancel, bttnUpload;

        beforeAll(function() {
          inptFile = element(by.id('prt-img-upld-file'));
          bttnPublish = element(by.id('prt-img-upld-publish'));
          bttnUnpublish = element(by.id('prt-img-upld-bttn-upload'));
          bttnCancel = element(by.id('prt-img-upld-bttn-cancel'));
          bttnUpload = element(by.id('prt-img-upld-bttn-upload'));
        });

        beforeEach(function() {
          bttnAddImage.click();
        });

        describe('Dialog:', function() {

          it('should be displayed', function() {
            expect(inptFile.isPresent()).toBeTruthy();
          });

          it('can be closed', function() {
            bttnCancel.click();
            expect(inptFile.isPresent()).toBeFalsy();
          });

          it('should has an initial state', function() {
            expect(inptFile.isPresent()).toBeTruthy();
            expect(inptFile.isDisplayed()).toBeTruthy();
            expect(inptFile.isEnabled()).toBeTruthy();
            expect(bttnPublish.isPresent()).toBeTruthy();
            expect(bttnPublish.isDisplayed()).toBeTruthy();
            expect(bttnPublish.isEnabled()).toBeTruthy();
            expect(bttnUnpublish.isPresent()).toBeTruthy();
            expect(bttnUnpublish.isDisplayed()).toBeTruthy();
            expect(bttnUnpublish.isEnabled()).toBeFalsy();
            expect(bttnCancel.isPresent()).toBeTruthy();
            expect(bttnCancel.isDisplayed()).toBeTruthy();
            expect(bttnCancel.isEnabled()).toBeTruthy();
            expect(bttnUpload.isPresent()).toBeTruthy();
            expect(bttnUpload.isDisplayed()).toBeTruthy();
            expect(bttnUpload.isEnabled()).toBeFalsy();
          });

          it('should be possible to mark image as \'Publish\' and \'Unpublish\'',
            function() {
              bttnUnpublish.click();
              expect(bttnPublish.isPresent()).toBeTruthy();
              expect(bttnPublish.isDisplayed()).toBeTruthy();
              expect(bttnPublish.isEnabled()).toBeTruthy();
              expect(bttnUnpublish.isPresent()).toBeTruthy();
              expect(bttnUnpublish.isDisplayed()).toBeTruthy();
              expect(bttnUnpublish.isEnabled()).toBeFalsy();
            }
          );

          it('should be possible to choose an image for upload', function() {
            var attachment = path.resolve(__dirname, '../resources/part.jpg');
            inptFile.sendKeys(attachment);
            browser.wait(EC.elementToBeClickable(bttnUpload), 3000, 'Button ' +
              '\'Upload\' has not been enabled when file to upload ' +
              'was choosed.');
          });

          it('should be possible to upload image and then delete it',
            function() {
              var dlgConfirmImgDel = element(
                by.css('.remove-image-confirmation-window'));
              var attachment = path.resolve(__dirname, '../resources/part.jpg');
              inptFile.sendKeys(attachment);
              browser.wait(EC.elementToBeClickable(bttnUpload), 3000, 'Button ' +
                '\'Upload\' has not been enabled when file to upload ' +
                'was choosed.');
              bttnUpload.click();
              // Check that dialog window is closed.
              expect(inptFile.isPresent()).toBeFalsy();
              expect(browser.getCurrentUrl())
                .toBe('http://localhost:8080/part/64956');
              browser._selectDropdownbyNum(elmDisplayPerPage, 4); // all images
              // check that image was added
              expect(ulImagesList.all(by.tagName('li')).count()).toBe(3);
              var lastImgItm = ulImagesList.all(by.tagName('li')).last();
              var bttnDelLastImg = lastImgItm.element(by.tiButton('Delete'));
              expect(bttnDelLastImg.isPresent()).toBeTruthy();
              expect(bttnDelLastImg.isDisplayed()).toBeTruthy();
              expect(bttnDelLastImg.isEnabled()).toBeTruthy();
              expect(dlgConfirmImgDel.isPresent()).toBeFalsy();
              bttnDelLastImg.click();
              expect(dlgConfirmImgDel.isDisplayed()).toBeTruthy();
              var bttnYes = dlgConfirmImgDel.element(by.tiButton('Yes'));
              expect(bttnYes.isPresent()).toBeTruthy();
              expect(bttnYes.isDisplayed()).toBeTruthy();
              expect(bttnYes.isEnabled()).toBeTruthy();
              bttnYes.click();
              expect(dlgConfirmImgDel.isPresent()).toBeFalsy();
              expect(ulImagesList.all(by.tagName('li')).count()).toBe(2);
            }
          );

        });

      });

    });

  });

  describe('Tab - Critical Dimensions:', function() {

    var tabArea, lnkToggleLegend, bttnAddReplaceImage, elmHideBlank,
      elmInlineLayout, inpFilter, bttnModifyAll, bttnSaveAll, bttnUndoAll,
      bttnCancelAll, rows, imgLegend, imgNoLegend;

    beforeAll(function() {
      tabArea = element(by.id('critical_dimensions'));
      lnkToggleLegend = element(by.id('cd-ctrl-toggle-legend'));
      bttnAddReplaceImage = element(by.id('cd-bttn-add-replcace-image'));
      elmHideBlank = element(by.id('cd-ctrl-hide-blank'));
      elmInlineLayout = element(by.id('cd-ctrl-inline-layout'));
      inpFilter = element(by.id('cd-inpt-filter'));
      bttnModifyAll = element(by.id('cd-bttn-modify-all'));
      bttnSaveAll = element(by.id('cd-bttn-save-all'));
      bttnUndoAll = element(by.id('cd-bttn-undo-all'));
      bttnCancelAll = element(by.id('cd-bttn-cancel-all'));
      rows = element.all(by.repeater('d in toDisplay'));
      imgLegend = element(by.id('cd-legend-image'));
      imgNoLegend = element(by.id('cd-legend-noimage'));
    });

    beforeEach(function() {
      browser.getCurrentUrl();
      browser.get('http://localhost:8080/part/49654');
      tabDimensions.click();
      // Activating of tabs is poorly detected by Protractor
      // so line below is a double check that tab was activated
      // and all controls are visible.
      browser.wait(EC.visibilityOf(tabArea), 3000,
        'Can\'t open a tab \'CriticalDimensions\'');
    });

    it('should has an initial state', function() {
      expect(lnkToggleLegend.isPresent()).toBeTruthy();
      expect(lnkToggleLegend.isDisplayed()).toBeTruthy();
      expect(lnkToggleLegend.isEnabled()).toBeTruthy();
      expect(elmHideBlank.isPresent()).toBeTruthy();
      expect(elmHideBlank.isDisplayed()).toBeTruthy();
      expect(elmHideBlank.isEnabled()).toBeTruthy();
      expect(elmHideBlank.evaluate('opts.hideBlank')).toBeTruthy(); // checked
      expect(elmInlineLayout.isPresent()).toBeTruthy();
      expect(elmInlineLayout.isDisplayed()).toBeTruthy();
      expect(elmInlineLayout.isEnabled()).toBeTruthy();
      expect(elmInlineLayout.evaluate('opts.inlineLayout')).toBeTruthy();
      expect(inpFilter.isPresent()).toBeTruthy();
      expect(inpFilter.isDisplayed()).toBeTruthy();
      expect(inpFilter.isEnabled()).toBeTruthy();
      expect(inpFilter.evaluate('opts.filter')).toBe('');
      expect(bttnModifyAll.isPresent()).toBeTruthy();
      expect(bttnModifyAll.isDisplayed()).toBeTruthy();
      expect(bttnModifyAll.isEnabled()).toBeTruthy();
      expect(bttnSaveAll.isPresent()).toBeTruthy();
      expect(bttnSaveAll.isDisplayed()).toBeFalsy();
      expect(bttnUndoAll.isPresent()).toBeTruthy();
      expect(bttnUndoAll.isDisplayed()).toBeFalsy();
      expect(bttnCancelAll.isPresent()).toBeTruthy();
      expect(bttnCancelAll.isDisplayed()).toBeFalsy();
      expect(rows.count()).toBe(15);
      expect(imgLegend.isPresent()).toBeTruthy();
      expect(imgLegend.isDisplayed()).toBeFalsy();
      expect(imgNoLegend.isPresent()).toBeTruthy();
      expect(imgNoLegend.isDisplayed()).toBeFalsy();
    });

    it('should be possible to show empty lines', function() {
      elmHideBlank.click();
      expect(rows.count()).toBe(19);
    });

    it('should be possible to show non-inline layout', function() {
      elmInlineLayout.click();
      expect(rows.count()).toBe(23);
    });

    it('should filter rows', function() {
      inpFilter.sendKeys('dia');
      expect(rows.count()).toBe(7);
    });

    describe('Legend image CRUD:', function() {

      var inptFile, bttnCancel, bttnUpload, dlgUploadLegend;

      beforeAll(function() {
        inptFile = element(by.id('cd-file'));
        bttnCancel = element(by.id('cd-dlg-upload-btn-cancel'));
        bttnUpload = element(by.id('cd-dlg-upload-btn-upload'));
        dlgUploadLegend = element(by.id('dlgUploadLegend'));
      });

      beforeEach(function() {
        lnkToggleLegend.click();
      });

      it('should has an initial state', function() {
        // expect(imgNoLegend.isDisplayed()).toBeTruthy();
        // expect(imgLegend.isDisplayed()).toBeFalsy();
        expect(bttnAddReplaceImage.isPresent()).toBeTruthy();
        expect(bttnAddReplaceImage.isDisplayed()).toBeTruthy();
        expect(bttnAddReplaceImage.isEnabled()).toBeTruthy();
        expect(dlgUploadLegend.isDisplayed()).toBeFalsy();
      });

      it('should open and close upload image dialog', function() {
        bttnAddReplaceImage.click();
        browser.wait(EC.visibilityOf(dlgUploadLegend), 3000,
          'A dialog to upload an image has not been displayed');
        expect(dlgUploadLegend.isDisplayed()).toBeTruthy();
        expect(bttnCancel.isPresent()).toBeTruthy();
        expect(bttnCancel.isDisplayed()).toBeTruthy();
        expect(bttnCancel.isEnabled()).toBeTruthy();
        expect(bttnUpload.isPresent()).toBeTruthy();
        expect(bttnUpload.isDisplayed()).toBeTruthy();
        expect(bttnUpload.isEnabled()).toBeFalsy();
        bttnCancel.click();
        expect(dlgUploadLegend.isDisplayed()).toBeFalsy();
      });

      it('should upload an image', function() {
        bttnAddReplaceImage.click();
        var attachment = path.resolve(__dirname, '../resources/part.jpg');
        inptFile.sendKeys(attachment);
        browser.wait(EC.elementToBeClickable(bttnUpload), 5000,
          'Button \'Upload\' is disabled.');
        expect(bttnUpload.isEnabled()).toBeTruthy();
        bttnUpload.click();
        expect(dlgUploadLegend.isDisplayed()).toBeFalsy();
        expect(browser.getCurrentUrl())
          .toBe('http://localhost:8080/part/49654');
        expect(imgLegend.isDisplayed()).toBeTruthy();
        expect(imgNoLegend.isDisplayed()).toBeFalsy();
      });

    });

    describe('Edit:', function() {

      beforeEach(function() {
        bttnModifyAll.click();
      });

      it('should have an initial state', function() {
        expect(bttnModifyAll.isDisplayed()).toBeFalsy();
        expect(bttnSaveAll.isPresent()).toBeTruthy();
        expect(bttnSaveAll.isDisplayed()).toBeTruthy();
        expect(bttnSaveAll.isEnabled()).toBeFalsy();
        expect(bttnUndoAll.isPresent()).toBeTruthy();
        expect(bttnUndoAll.isDisplayed()).toBeTruthy();
        expect(bttnUndoAll.isEnabled()).toBeFalsy();
        expect(bttnCancelAll.isPresent()).toBeTruthy();
        expect(bttnCancelAll.isDisplayed()).toBeTruthy();
        expect(bttnCancelAll.isEnabled()).toBeTruthy();
        expect(elmHideBlank.isEnabled()).toBeFalsy();
        expect(elmInlineLayout.isEnabled()).toBeFalsy();
        expect(inpFilter.isEnabled()).toBeFalsy();
      });

      it('should return to r/o mode when button \'Cancel All\' is pressed',
        function() {
          bttnCancelAll.click();
          expect(bttnModifyAll.isDisplayed()).toBeTruthy();
          expect(bttnSaveAll.isPresent()).toBeTruthy();
          expect(bttnSaveAll.isDisplayed()).toBeFalsy();
          expect(bttnUndoAll.isPresent()).toBeTruthy();
          expect(bttnUndoAll.isDisplayed()).toBeFalsy();
          expect(bttnCancelAll.isPresent()).toBeTruthy();
          expect(bttnCancelAll.isDisplayed()).toBeFalsy();
          expect(elmHideBlank.isEnabled()).toBeTruthy();
          expect(elmInlineLayout.isEnabled()).toBeTruthy();
          expect(inpFilter.isEnabled()).toBeTruthy();
        }
      );

      describe('Dimension-enumeration:', function() {

        var dynCsText, dynCsCtrl, dynCsBttnUndo, dynCsBttnCancel,
          dynCsBttnModify;

        beforeAll(function() {
          var tds = rows.first().all(by.tagName('td'));
          dynCsText = tds.get(1).element(by.tagName('span'));
          dynCsCtrl = tds.get(1).element(by.name('dynCs'));
          dynCsBttnUndo = tds.get(2).element(by.tiButton('Undo'));
          dynCsBttnCancel = tds.get(2).element(by.tiButton('Cancel'));
          dynCsBttnModify = tds.get(2).element(by.tiButton('Modify'));
        });

        it('should has an initial state', function() {
          expect(dynCsText.isDisplayed()).toBeFalsy();
          expect(dynCsCtrl.isPresent()).toBeTruthy();
          expect(dynCsCtrl.isDisplayed()).toBeTruthy();
          expect(dynCsCtrl.isEnabled()).toBeTruthy();
          expect(dynCsBttnUndo.isPresent()).toBeTruthy();
          expect(dynCsBttnUndo.isDisplayed()).toBeTruthy();
          expect(dynCsBttnUndo.isEnabled()).toBeFalsy();
          expect(dynCsBttnCancel.isPresent()).toBeTruthy();
          expect(dynCsBttnCancel.isDisplayed()).toBeTruthy();
          expect(dynCsBttnCancel.isEnabled()).toBeTruthy();
          expect(dynCsBttnModify.isPresent()).toBeTruthy();
          expect(dynCsBttnModify.isDisplayed()).toBeFalsy();
        });

        it('should revert to r/o mode when button \'Cancel\' is pressed',
          function() {
            dynCsBttnCancel.click();
            expect(dynCsText.isDisplayed()).toBeTruthy();
            expect(dynCsText.getText()).toBe('DYNAMIC');
            expect(dynCsCtrl.isPresent()).toBeTruthy();
            expect(dynCsCtrl.isDisplayed()).toBeFalsy();
            expect(dynCsBttnUndo.isPresent()).toBeTruthy();
            expect(dynCsBttnUndo.isDisplayed()).toBeFalsy();
            expect(dynCsBttnCancel.isPresent()).toBeTruthy();
            expect(dynCsBttnCancel.isDisplayed()).toBeFalsy();
            expect(dynCsBttnModify.isPresent()).toBeTruthy();
            expect(dynCsBttnModify.isDisplayed()).toBeTruthy();
            expect(dynCsBttnModify.isEnabled()).toBeTruthy();
          }
        );

        it('should revert to r/o mode when button \'Cancel All\' is pressed',
          function() {
            bttnCancelAll.click();
            expect(dynCsText.isDisplayed()).toBeTruthy();
            expect(dynCsText.getText()).toBe('DYNAMIC');
            expect(dynCsCtrl.isPresent()).toBeTruthy();
            expect(dynCsCtrl.isDisplayed()).toBeFalsy();
            expect(dynCsBttnUndo.isPresent()).toBeTruthy();
            expect(dynCsBttnUndo.isDisplayed()).toBeFalsy();
            expect(dynCsBttnCancel.isPresent()).toBeTruthy();
            expect(dynCsBttnCancel.isDisplayed()).toBeFalsy();
            expect(dynCsBttnModify.isPresent()).toBeTruthy();
            expect(dynCsBttnModify.isDisplayed()).toBeTruthy();
            expect(dynCsBttnModify.isEnabled()).toBeTruthy();
          }
        );

        it('should revert changes when button \'Undo\' is pressed',
          function() {
            browser._selectDropdownbyNum(dynCsCtrl, 3); // CARBON SEAL
            dynCsBttnUndo.click();
            expect(dynCsText.isDisplayed()).toBeFalsy();
            expect(dynCsCtrl.isPresent()).toBeTruthy();
            expect(dynCsCtrl.isDisplayed()).toBeTruthy();
            expect(dynCsCtrl.isEnabled()).toBeTruthy();
            expect(dynCsCtrl.evaluate('editedPart[d.jsonName].val'))
              .toBe('DYNAMIC');
            expect(dynCsBttnUndo.isPresent()).toBeTruthy();
            expect(dynCsBttnUndo.isDisplayed()).toBeTruthy();
            expect(dynCsBttnCancel.isPresent()).toBeTruthy();
            expect(dynCsBttnCancel.isDisplayed()).toBeTruthy();
            expect(dynCsBttnModify.isPresent()).toBeTruthy();
            expect(dynCsBttnModify.isDisplayed()).toBeFalsy();
          }
        );

        it('should revert changes when button \'Undo All\' is pressed',
          function() {
            browser._selectDropdownbyNum(dynCsCtrl, 3); // CARBON SEAL
            bttnUndoAll.click();
            expect(dynCsText.isDisplayed()).toBeFalsy();
            expect(dynCsCtrl.isPresent()).toBeTruthy();
            expect(dynCsCtrl.isDisplayed()).toBeTruthy();
            expect(dynCsCtrl.isEnabled()).toBeTruthy();
            expect(dynCsCtrl.evaluate('editedPart[d.jsonName].val'))
              .toBe('DYNAMIC');
            expect(dynCsBttnUndo.isPresent()).toBeTruthy();
            expect(dynCsBttnUndo.isDisplayed()).toBeTruthy();
            expect(dynCsBttnCancel.isPresent()).toBeTruthy();
            expect(dynCsBttnCancel.isDisplayed()).toBeTruthy();
            expect(dynCsBttnModify.isPresent()).toBeTruthy();
            expect(dynCsBttnModify.isDisplayed()).toBeFalsy();
          }
        );

        it('should save changes when button \'Save All\' is pressed',
          function() {
            browser._selectDropdownbyNum(dynCsCtrl, 3); // CARBON SEAL
            bttnSaveAll.click();
            expect(dynCsText.isDisplayed()).toBeTruthy();
            expect(dynCsCtrl.isPresent()).toBeTruthy();
            expect(dynCsCtrl.isDisplayed()).toBeFalsy();
            expect(dynCsCtrl.evaluate('editedPart[d.jsonName].val'))
              .toBe('CARBON SEAL');
            expect(dynCsBttnUndo.isPresent()).toBeTruthy();
            expect(dynCsBttnUndo.isDisplayed()).toBeFalsy();
            expect(dynCsBttnCancel.isPresent()).toBeTruthy();
            expect(dynCsBttnCancel.isDisplayed()).toBeFalsy();
            expect(dynCsBttnModify.isPresent()).toBeTruthy();
            expect(dynCsBttnModify.isDisplayed()).toBeTruthy();
            // revert original values
            dynCsBttnModify.click();
            browser._selectDropdownbyNum(dynCsCtrl, 2); // DYNAMIC
            bttnSaveAll.click();
            expect(dynCsCtrl.evaluate('editedPart[d.jsonName].val'))
              .toBe('DYNAMIC');
          }
        );

      });

    });

  });

  describe('Tab - Turbo Types:', function() {

    var tabArea, bttnAddType, rows;

    beforeAll(function() {
      tabArea = element(by.id('turbo_types'));
      bttnAddType = element(by.id('btn-add-turbo-type'));
      rows = element.all(by.repeater('turboType in $data'));
    });

    beforeEach(function() {
      tabTurboTypes.click();
      // Activating of tabs is poorly detected by Protractor
      // so line below is a double check that tab was activated
      // and all controls are visible.
      browser.wait(EC.visibilityOf(tabArea), 3000,
        'Can\'t open a tab \'Turbo Types\'');
    });

    it('should have an initial state', function() {
      expect(bttnAddType.isPresent()).toBeTruthy();
      expect(bttnAddType.isDisplayed()).toBeTruthy();
      expect(bttnAddType.isEnabled()).toBeTruthy();
      expect(rows.count()).toBe(0);
    });

  });

  describe('Tab - Audit Log:', function() {

    var tabArea, rows;

    beforeAll(function() {
      tabArea = element(by.id('audit_log'));
      rows = element.all(by.repeater('rec in $data'));
    });

    beforeEach(function() {
      tabAuditLog.click();
      // Activating of tabs is poorly detected by Protractor
      // so line below is a double check that tab was activated
      // and all controls are visible.
      browser.wait(EC.visibilityOf(tabArea), 3000,
        'Can\'t open a tab \'Audit Log\'');
    });

    it('should display a records in the table', function() {
      expect(rows.count()).toBe(0);
    });

  });

  describe('Tab - Prices:', function() {

    var tabArea, rows;

    beforeAll(function() {
      tabArea = element(by.id('prices'));
      rows = element.all(by.repeater('(n, v) in prices.prices'));
    });

    beforeEach(function() {
      tabPrices.click();
      // expect(tabPrices.isDisplayed()).toBeTruthy();
      // Activating of tabs is poorly detected by Protractor
      // so line below is a double check that tab was activated
      // and all controls are visible.
      browser.wait(EC.visibilityOf(tabArea), 3000,
        'Can\'t open a tab \'Prices\'');
    });

    it('should display a records in the table', function() {
      expect(rows.count()).toBe(12);
    });

  });

  describe('Tab - Also Bought:', function() {

    var tabArea, rows;

    beforeAll(function() {
      tabArea = element(by.id('also_bought'));
      rows = element.all(by.repeater('r in $data'));
    });

    beforeEach(function() {
      tabAlsoBought.click();
      // Activating of tabs is poorly detected by Protractor
      // so line below is a double check that tab was activated
      // and all controls are visible.
      browser.wait(EC.visibilityOf(tabArea), 3000,
        'Can\'t open a tab \'Also Bought\'');
    });

    it('should display a records in the table', function() {
      expect(rows.count()).toBe(25);
    });

  });

});
