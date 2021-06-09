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

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

/**
 * A sub-class of Runnable that handles the actual execution of each test.
 */
final class TestExecutorTask implements Runnable {
    private IntegrationTest diagnosticTest;

    private List<Consumer<TestResult>> testResultListenerList;

    private TestResultFactory testResultFactory;

    /**
     * @param diagnosticTest - An instance of DiagnosticTest that is ready to be executed.
     * @param testResultListenerList - The list of listeners to be notified upon test completion.
     */
    public TestExecutorTask(IntegrationTest diagnosticTest,
                            List<Consumer<TestResult>> testResultListenerList,
                            TestResultFactory testResultFactory) {
        this.diagnosticTest = diagnosticTest;
        this.testResultListenerList = testResultListenerList;
        this.testResultFactory = testResultFactory;
    }

    /**
     * Obligatory method from Runnable interface. Handles the actual execution of the test.
     */
    @Override
    public void run() {
        String testName = diagnosticTest.getName();
        Instant beforeTest = Instant.now();
        try {
            diagnosticTest.setup();
            diagnosticTest.test();
            diagnosticTest.cleanup();
            this.notify(testResultFactory.pass(testName, beforeTest, Instant.now()));
        } catch (AssertionError e) {
            this.notify(testResultFactory.fail(testName, e.getMessage(), beforeTest, Instant.now()));
        } catch (Throwable t) {
            this.notify(testResultFactory.error(testName, t, beforeTest, Instant.now()));
        }
    }

    private void notify(TestResult testResult) {
        testResultListenerList.stream()
                .forEach(l -> l.accept(testResult));
    }
}
