// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

exports.config = {
  multiCapabilities: [
    {
      browserName: 'chrome',
      seleniumAddress: 'http://localhost:4444/wd/hub',
      specs: ['spec/*.js'],
      chromeOptions: {
        prefs: {
          'credentials_enable_service': false,
          'profile': {
            'password_manager_enabled': false
          }
        },
        args: [
          '--disable-cache',
          '--disable-application-cache',
          '--disable-offline-load-stale-cache',
          '--disk-cache-size=0',
          '--v8-cache-options=off'
        ]
      }
    },
//    {
//      browserName: 'firefox',
//      seleniumAddress: 'http://localhost:4444/wd/hub',
//      specs: ['spec/*.js'],
//      firefoxOptions: {
//        prefs: {
//          'browser.cache.disk.enable': false,
//          'browser.cache.memory.enable': false,
//          'browser.cache.offline.enable': false,
//          //'network.http.use-cache': ,
//        }
//      }
//    }
  ],
  onPrepare: function() {

    var disableNgAnimate = function() {
      angular.module('disableNgAnimate', [])
        .run(['$animate', function($animate) {
          $animate.enabled(false);
        }]);
    };

    var disableCssAnimate = function() {
      angular
        .module('disableCssAnimate', [])
        .run(function() {
          var style = document.createElement('style');
          style.type = 'text/css';
          style.innerHTML = '* {' +
            '-webkit-transition: none !important;' +
            '-moz-transition: none !important' +
            '-o-transition: none !important' +
            '-ms-transition: none !important' +
            'transition: none !important' +
            '}';
          document.getElementsByTagName('head')[0].appendChild(style);
        });
    };

    browser.addMockModule('disableNgAnimate', disableNgAnimate);
    browser.addMockModule('disableCssAnimate', disableCssAnimate);

    browser._selectDropdownbyNum = function (element, optionNum) {
      /* A helper function to select in a dropdown control an option
      * with specified number.
      */
      return element.all(by.tagName('option')).then(
        function(options) {
          var opt = options[optionNum];
          opt.click();
        });
    };

    browser._selectReset = function (element) {
      return browser._selectDropdownbyNum(element, 0);
    };

    browser._pressEnter = function() {
      browser.actions().sendKeys(protractor.Key.ENTER).perform();
    };

    by.addLocator('tiButton',
      function(buttonText, parentElement, rootSelector) {
        if (!buttonText) {
          throw 'Label of a button must be specified.';
        }
        var using = parentElement || document,
          buttons = using.querySelectorAll('button.btn');
        var escapedBttnText = buttonText.replace(
          /[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
        var rgxp = new RegExp('\\s*' + escapedBttnText + '\\s*');
        return Array.prototype.filter.call(buttons, function(button) {
          return rgxp.test(button.innerText);
        });
      }
    );

    // Login.
    browser.get('http://localhost:8080');
    element(by.id('username')).sendKeys('pavlo.kurochka@zorallabs.com');
    element(by.id('password')).sendKeys('zoraltemp');
    element(by.id('bttn-login')).click();

  },
  onComplete: function() {
    // Logout.
    browser.get('http://localhost:8080/part/list');
    element(by.id('lnk-logout')).click();
  }
};
