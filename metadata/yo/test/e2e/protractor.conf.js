exports.config = {
  seleniumAddress: 'http://localhost:4444/wd/hub',
  //specs: ['spec/**/*.js'],
  //specs: ['spec/nav.js', 'spec/partsearch.js', 'spec/sourcelink.js'],
  specs: ['spec/users.js'],
  capabilities: {
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
  },
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
          options[optionNum].click();
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
