// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

'use strict';

describe('Services:', function() {

  var rows, firstRowTds, lastRowTds, checkboxes;

  beforeAll(function() {
    rows = element.all(by.repeater('srv in $data'));
    firstRowTds = rows.first().all(by.tagName('td'));
    lastRowTds = rows.last().all(by.tagName('td'));
    checkboxes = rows.all(by.model('requiredSource[srv.name]'));
  });

  beforeEach(function() {
    browser.getCurrentUrl();
    browser.get('http://localhost:8080/service/list');
    // Specs below checks some checkboxes in the column 'Required Source'.
    // This code resets all those checkboxes in expeced state -- all boxes
    // are uncheced except for services BOM and INTERCHANGE.
    checkboxes.map(function(elem) {
      elem.isSelected().then(function(checked) {
        elem.evaluate('srv.name').then(function(srvName) {
          if (srvName === 'BOM' || srvName === 'INTERCHANGE') {
            if (!checked) {
              elem.click(); // check
            }
          } else {
            if (checked) {
              elem.click(); // uncheck
            }
          }
        });
      });
    });
  });

  it('should have a list of services', function() {
    expect(rows.isPresent()).toBeTruthy();
    expect(rows.isDisplayed()).toBeTruthy();
    expect(rows.count()).toBe(9);
  });

  it('should have columns', function() {
    expect(firstRowTds.count()).toBe(3);
  });

  it('should have services with check \'Required Source\' column',
    function () {
      expect(checkboxes.count()).toBe(5);
      var checked = checkboxes.filter(
        function (elem) {
          return elem.isSelected();
        }
      );
      expect(checked.count()).toBe(2);
      expect(checked.first().evaluate('srv.name')).toBe('BOM');
      expect(checked.last().evaluate('srv.name')).toBe('INTERCHANGE');
    }
  );

  describe('PART:', function() {

    it('should display \'Link Source\' dialog when a new part is created',
      function() {
        var partCheckbox = checkboxes.filter(function(elem) {
          return elem.evaluate('srv.name').then(function(srvName) {
            return srvName === 'PART';
          });
          return defer;
        }).first();
        //expect(partCheckbox.isPresent()).toBeTruthy();
        //expect(partCheckbox.isDisplayed()).toBeTruthy();
        //expect(partCheckbox.isSelected()).toBeFalsy();
        partCheckbox.click();
        browser.get('http://localhost:8080/part/list');
        var bttnCreatePart = element(by.partialLinkText('Create Part'));
        bttnCreatePart.click();
        var cntrlPartType = element(by.model('selection.id'));
        //expect(cntrlPartType.isPresent()).toBeTruthy();
        var bttnCreate = element(by.partialButtonText('Create'));
        //expect(bttnCreate.isPresent()).toBeTruthy();
        browser._selectDropdownbyNum(cntrlPartType, 1); // Actuator
        bttnCreate.click(); // close popup and open a main form
        browser._selectDropdownbyNum(
          element(by.id('manufacturer')), 1); // Garret
        element(by.id('pn0')).sendKeys('UNIQUE-PART-NUMBER');
        var lnkSrcDlg = element(by.css('.modal-open'));
        expect(lnkSrcDlg.isPresent()).toBeFalsy();
        element(by.tiButton('Save')).click();
        expect(lnkSrcDlg.isPresent()).toBeTruthy();
      }
    );

    // TODO: other service types

  });

});
