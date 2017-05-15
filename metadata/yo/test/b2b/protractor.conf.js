exports.config = {
  seleniumAddress: 'http://localhost:4444/wd/hub',
  // specs: ['spec/**/*.js'],
  specs: ['spec/sourcelink.js'],
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
  }
};
