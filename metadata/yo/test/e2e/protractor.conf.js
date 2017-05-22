exports.config = {
  seleniumAddress: 'http://localhost:4444/wd/hub',
  //specs: ['spec/**/*.js'],
  specs: ['spec/sourcelink.js'/*, 'spec/nav.js'*/],
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
  },
  onComplete: function() {
    // Logout.
    browser.get('http://localhost:8080/part/list');
    element(by.id('lnk-logout')).click();
  }
};
