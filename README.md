#### Integration Test Framework

##### Description
This component provides a simple framework for integration testing. It's intended to be used in end-to-end system 
testing against a deployed system, with minimal mocking. It provides a framework for test execution and reporting, 
leaving the test-developer to use whichever tools they prefer for the actual testing. We follow the patterns established
by JUnit for assertions and matching.

##### Command Line Options
This component supports several arguments, all of which are optional. They are:

* `itest.threads` - the number of execution threads to run the tests with. Default is 1.
* `itest.tests` - a comma-separated list of the fully-qualified test names to be executed. Tests 
will be executed in the order given. If not provided all tests beans will be executed.
* `itest.result.runId` - A UUID that allows the caller to associate a group of tests with each 
other. Default is a randomly-chosen value.
* `itest.reporter.logger` - the name of a logger to report test results to. If not supplied then 
`LoggingIntegrationTestReporter` logger will be used.
* `itest.reporter.url` - the url of an endpoint to send test results to. By default, no attempt will
be made send test results to any endpoint.

##### Implementing Tests
In this framework, tests are POJOs. Each class is a single test. Common code can reside in a parent class. Here's how 
you implement a test:

1) Create a test class which implements the `IntegrationTest` interface. This interface contains 3
   methods, `setup()`, `test()` and `cleanup()` which are called in that order. `setup()` and 
   `cleanup()` are guaranteed to be called. `test()` will not be called in the event that `setup()`
   throws an exception.
2) Make each test class a Spring Bean - Either create an `@Configuration` class which instantiates your test as a
   bean or add the `@Component` annotation directly to the test class. Spring will handle aggregating those
   tests.
3) Use the standard JUnit assertions classes to validate/verify test results.

##### Docker Image
Tests are executed using the `codice-itest` Docker image which is built as part of `mvn clean install`.

The `codice-itest` image defines a bind volume so that tests, and their dependencies, can be mapped there. 
This will make the classes available on the JVM classpath. However, in order for the classes to be component-scanned by 
Spring, we need to give Spring the package hierarchy from which to start scanning. We do this with the 
`itest.packagescan` configuration property, which defaults to `org.codice`. 

###### Example Test Execution
In order to execute the tests, the Docker image needs access to the jar that contains the tests as well as any 
dependencies. We use a Docker bind-volume at `/app/tests` to do this. This Docker command will execute all tests under 
`com.mytests` from the jar files found in the `target` folder under the current working directory:

```
docker run -e itest.packagescan=com.mytests -v ${PWD}/target:/app/tests codice-itest
```

This approach works well for the development phase of tests, but we can also extend the `codice-itest` Docker image to 
eliminate the need for both the volume and the environment variable, which makes the tests easier to deploy and run.
Below is an example `pom.xml` block which does this using the `jib` maven plugin.

```
            <plugin>
                <groupId>com.google.cloud.tools</groupId>
                <artifactId>jib-maven-plugin</artifactId>
                <configuration>
                    <from>
                        <image>docker.io/codice/itest:0.0.1</image>
                    </from>
                    <to>
                        <image>integrationtest-example</image>
                        <tags>
                            <tag>0.0.1</tag>
                            <tag>latest</tag>
                        </tags>
                    </to>
                    <container>
                        <entrypoint>INHERIT</entrypoint>
                        <environment>
                            <itest.packagescan>com.mytests</itest.packagescan>
                        </environment>
                    </container>
                </configuration>
                <executions>
                    <execution>
                        <id>build-image</id>
                        <phase>package</phase>
                        <goals>
                            <goal>dockerBuild</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

```

Now we can run these tests with a simple command:

```
    docker run codice-itest-example
```

For tests that require environment-specific configuration, those can be placed into an `application.properties` or
`application.yaml` file on the local system and mapped into the `/app/config` Docker volume. The command below assumes 
that a configuration file was placed into the `config` folder under the current working directory:

```
    docker run -v ${PWD}/config:/app/config codice-itest-example
```
