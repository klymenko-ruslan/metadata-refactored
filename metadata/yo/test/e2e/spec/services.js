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

  xdescribe('PART:', function() {

    it('should display \'Link Source\' dialog when a new part is created',
      function() {
        var partCheckbox = checkboxes.filter(function(elem) {
          return elem.evaluate('srv.name').then(function(srvName) {
            return srvName === 'PART';
          });
          return defer;
        });
        expect(partCheckbox.count()).toBe(1);
        partCheckbox = partCheckbox.first();
        //expect(partCheckbox.isPresent()).toBeTruthy();
        //expect(partCheckbox.isDisplayed()).toBeTruthy();
        //expect(partCheckbox.isSelected()).toBeFalsy();
        partCheckbox.click();
        //expect(partCheckbox.isSelected()).toBeTruthy();
        browser.get('http://localhost:8080/part/list');
        var bttnCreatePart = element(by.partialLinkText('Create Part'));
        bttnCreatePart.click();
        var cntrlPartType = element(by.model('selection.id'));
        expect(cntrlPartType.isPresent()).toBeTruthy();
        var bttnCreate = element(by.partialButtonText('Create...'));
        expect(bttnCreate.isPresent()).toBeTruthy();
        browser._selectDropdownbyNum(cntrlPartType, 2);
        bttnCreate.click();
      }
    );

  });

});
