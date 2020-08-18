#### Integration Test Framework

##### Description
This component provides a simple framework for integration testing. It uses the dependency 
inversion pattern to separate the test business logic from the test execution logic. 

##### Command Line Options
This component supports several command line arguments, all of which are optional. They are:

* `itest.threads` - the number of execution threads to run the tests with. Default is 1.
* `itest.tests` - a comma-separated list of the fully-qualified test names to be executed. Tests 
will be executed in the order given. If not provided all tests beans will be executed.
* `itest.result.runId` - A UUID that allows the caller to associate a group of tests with each 
other. Default is a randomly-chosen value.
* `itest.reporter.logger` - the name of a logger to report test results to. If not supplied then 
`org.codice.integrationtest.framework.reporter.LoggingDiagnosticTestReporter` logger will be used.
* `itest.reporter.url` - the url of an endpoint to send test results to. By default, no attempt will
be made send test results to any endpoint.

##### Implementing Tests
In this framework, tests are POJOs. Each class is a single test. Common code can reside in a 
parent class. Here's how you implement a test:

1) Create a test class which implements the `DiagnosticTest` interface. This interface contains 3
   methods, `setup()`, `test()` and `cleanup()` which are called in that order. `setup()` and 
   `cleanup()` are guaranteed to be called. `test()` will not be called in the event that `setup()`
   throws an exception.
2) Make each test class a Spring Bean - Create an `@Configuration` class that creates your test as a
   bean or add the `@Component` annotation to the class itself. Spring will handle aggregating those
   tests.
3) Use the standard JUnit assertions classes to validate/verify test results.
4) Add an application.properties file with the name of the package that your tests are
   stored in (e.g. `testPackage=org.codice.app`). If you have multiple packages, you
   can use a wildcard to add them from the highest common point in the package hierarchy (e.g. 
   `itest.package.scan=org.codice`).

###### *See the example code in `../diagnostic-example`
