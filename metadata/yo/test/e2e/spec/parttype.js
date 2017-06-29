// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

var path = require('path');
var EC = protractor.ExpectedConditions;

describe('Part Types:', function() {

  describe('List:', function() {

    var rows, tdsFirst, bttnFirstView;

    beforeAll(function() {
      browser.getCurrentUrl();
      browser.get('http://localhost:8080/parttype/list');
      rows = element.all(by.repeater('pt in $data'));
      tdsFirst = rows.first().all(by.tagName('td'));
      bttnFirstView = tdsFirst.last().element(by.tagName('a'));
    });

    it('should has rows', function() {
      expect(rows.isPresent()).toBeTruthy();
      expect(rows.count()).toBe(10);
      expect(tdsFirst.count()).toBe(6);
      expect(bttnFirstView.isPresent()).toBeTruthy();
    });

    it('should open view details on click of \'View\'', function() {
      expect(browser.getCurrentUrl())
        .toBe('http://localhost:8080/parttype/list');
      bttnFirstView.click();
      var url = browser.getCurrentUrl();
      expect(url).toMatch(/http:\/\/localhost:8080\/parttype\/\d+/);
    });

  });

  describe('View:', function() {

    var bttnAddReplaceImage;

    beforeAll(function() {
      browser.getCurrentUrl();
      browser.get('http://localhost:8080/parttype/30'); // Actuator
      bttnAddReplaceImage = element(by.partialButtonText(
        'Add/Replace Image...'));
    });

    it('should have a button \'Add/Replace Image...\'', function() {
      expect(bttnAddReplaceImage.isPresent()).toBeTruthy();
    });

    it('should upload an image', function() {
      var image2upload = path.resolve(__dirname, '../resources/washer.jpg');
      var dlg = element(by.id('dlgUploadLegend'));
      expect(dlg.isPresent()).toBeTruthy();
      expect(dlg.isDisplayed()).toBeFalsy();
      bttnAddReplaceImage.click();
      var bttnFile = element(by.id('bttnFile'));
      expect(bttnFile.isPresent()).toBeTruthy();
      bttnFile.sendKeys(image2upload);
      var bttnUpload = element(by.partialButtonText('Upload'));
      browser.wait(EC.visibilityOf(dlg), 5000, 'A dialog to upload image ' +
        'has not been displayed.');
      expect(bttnUpload.isPresent()).toBeTruthy();
      expect(bttnUpload.isEnabled()).toBeTruthy();
      bttnUpload.click();
      browser.wait(EC.invisibilityOf(dlg), 5000, 'A dialog to upload image ' +
        'has not been closed.');
    });

  });


});
