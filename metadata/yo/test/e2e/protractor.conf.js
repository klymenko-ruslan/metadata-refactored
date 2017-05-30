// vim: set fileencoding=utf-8 :
// vim: tabstop=2 expandtab shiftwidth=2 softtabstop=2

exports.config = {
  seleniumAddress: 'http://localhost:4444/wd/hub',
  //specs: ['spec/**/*.js'],
  //specs: ['spec/nav.js', 'spec/partsearch.js', 'spec/sourcelink.js'],
  //specs: ['spec/partsearch.js'],
  specs: ['spec/users.js'],
  multiCapabilities: [/*{
    browserName: 'chrome',
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
  },*/ {
    browserName: 'firefox',
    firefoxOptions: {
      prefs: {
        'browser.cache.disk.enable': false,
        'browser.cache.memory.enable': false,
        'browser.cache.offline.enable': false,
        //'network.http.use-cache': ,
      }
    }
  }],
  onPrepare: function() {
    // Login.
    browser.get('http://localhost:8080');
    element(by.id('username')).sendKeys('pavlo.kurochka@zorallabs.com');
    element(by.id('password')).sendKeys('zoraltemp');
    element(by.id('bttn-login')).click();
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
  },
  onComplete: function() {
    // Logout.
    browser.get('http://localhost:8080/part/list');
    element(by.id('lnk-logout')).click();
  }
};
