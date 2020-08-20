/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest;

import org.codice.itest.api.IntegrationTest;
import org.codice.itest.api.TestResult;
import org.codice.itest.api.TestResultFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * This Spring service executes any instances of `DiagnosticTest` that it finds in the
 * application context. It executes those tests in a parallel fashion using an 'ExecutorService'
 * but can be forced into serial execution by setting the value of '${threads}' to 1.
 *
 * If a value is given for ${itest.runId} then that value will be included in each 'TestResult'.
 * The 'Consumer<TestResult>` will be notified of success/failure/error upon the completion of
 * each test.
 */

@Component
final class IntegrationTestService implements CommandLineRunner {

    private Stream<IntegrationTest> tests;

    private List<Consumer<TestResult>> testResultListenerList;

    private ExecutorService executorService;

    private TestResultFactory testResultFactory;

    /**
     *
     * @param tests - The set of all DiagnosticTest objects found in the Spring application context.
     * @param testResultListenerList - A list of listeners to be notified upon test completion.
     * @param executorService - Used to allow tests to be executed in parallel.
     */
    public IntegrationTestService(Stream<IntegrationTest> tests,
                                  List<Consumer<TestResult>> testResultListenerList,
                                  TestResultFactory testResultFactory,
                                  ExecutorService executorService) {
        this.tests = tests;
        this.testResultListenerList = testResultListenerList;
        this.testResultFactory = testResultFactory;
        this.executorService = executorService;
    }

    /**
     * This method is called after Spring has completed initializing the application context. It
     * delegates the actual execution of each test to a 'TestExecutorTask' instance.
     *
     * @throws InterruptedException - When interrupted while waiting for a test to complete.
     */
    public void run(String[] args) throws InterruptedException {
        this.tests.forEach(test -> executorService.execute(new TestExecutorTask(test,
                testResultListenerList, testResultFactory)));
        executorService.shutdown();
        executorService.awaitTermination(120, TimeUnit.SECONDS);
    }
}
