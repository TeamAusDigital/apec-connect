import jasmineReporters from 'jasmine-reporters';

var reporter = new jasmineReporters.JUnitXmlReporter({
  // Jasmine report with Jest may be broken with consolidateAll as true: https://github.com/larrymyers/jasmine-reporters/issues/149
  consolidateAll: false,
  savePath: 'test-output/',
  filePrefix: 'datapos-client-portal-unit-'
});
jasmine.getEnv().addReporter(reporter);
