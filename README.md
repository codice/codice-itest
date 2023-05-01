# Codice ITest 3.0

## Description
Version 3 of `codice-itest` is an update of versions which includes updates to the Java version, Spring versions, 
bug fixes, property re-naming, etc.  The differences between 2 and 3 are given here. If you want to start from a clean 
ITest project, you might want to take a look at: [QuickStart](./QUICKSTART.md).

This component provides a simple framework for integration testing. It's intended to be used in end-to-end system
testing against a deployed system, with minimal mocking. It provides a framework for test execution and reporting,
leaving the test-developer to use whichever tools they prefer for the actual testing. We follow the patterns established
by JUnit for assertions and matching.

## Command line options
The command line options are largely the same as they were in version 2, but renamed. The table below shows how the
names were mapped from v2 to v3:

| V2 Name                     | V3 Name                          | Default Value     |
|-----------------------------|----------------------------------|-------------------|
| itest.threads               | codice.itest.threads             | 1                 |
| itest.tests                 | codice.itest.tests               | (all tests)       |
| itest.result.runId          | codice.itest.runId               | A random UUID     |
| itest.reporter.logger       | codice.itest.loggerName          | codice.itest      |
| itest.max.execution.minutes | codice.itest.maxExecutionMinutes | Integer.MAX_VALUE |

There is no longer an `itest.packagescan` value. The motivation for that value has been eliminated by making the test
developer responsible for supplying the Spring Boot Application code.

### Property definitions:

* `codice.itest.threads` - the number of execution threads to run the tests with. Default is 1.
* `codice.itest.tests` - a comma-separated list of the test names to be executed. Tests will be executed in the order
  given. If not provided all tests beans will be executed.
* `codice.itest.runId` - A UUID that allows the caller to associate a group of tests with each other. Default is a
  randomly-chosen value.
* `codice.itest.loggerName` - the name of a logger to report test results to. If not supplied then
  `codice.itest` logger will be used.
* `codice.itest.maxExecutionMinutes` - the maximum number of minutes tests will run for. Default is 10.

Note on test naming: The `IntegrationTest` API allows the developer to optionally name the test. If a name is given,
then it can be used for the elements in the `codice.itest.tests` parameter. If no name is given then the class name
is used. When integration tests are defined anonymously, this can be difficult to use so it's recommended to give tests
names, especially if they aren't being defined as a top-level class.

## Implementing ITests
In this framework, tests are Spring Beans. Each class is a single test. Common code can reside in a parent class. 
Here's how you implement a test:

1) Create a test class which implements the `IntegrationTest` interface. This interface contains 4
   methods, `getName()`, `setup()`, `test()` and `cleanup()` which are called in that order. `setup()` and
   `cleanup()` are guaranteed to be called. `test()` will not be called in the event that `setup()`
   throws an exception.
2) Make each test class a Spring Bean - Either create an `@Configuration` class which instantiates your test as a
   bean or add the `@Component` annotation directly to the test class. Spring will handle aggregating those
   tests.
3) Use the standard JUnit assertions classes to validate/verify test results.

## Build and execution
1) Build the project:

```shell
mvn clean install
```

2) Run the project:

```shell
java -jar target/hello-world-test-1.0.0.jar
```

## [Optional] Build and run as a docker container:

1) Build container:

```shell 
mvn jib:dockerBuild
```

2) Run container:

```shell
docker images | grep hello-world | tr -s ' ' | cut -d " " -f3 | uniq
```