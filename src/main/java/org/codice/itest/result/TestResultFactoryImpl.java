/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest.result;

import java.time.Instant;
import java.util.UUID;

import org.codice.itest.api.TestResult;
import org.codice.itest.api.TestResultFactory;
import org.codice.itest.api.TestStatus;
import org.slf4j.MDC;

/**
 * Implements TestResultFactory to create appropriate instances of the TestResult interface to the
 * main application.
 */
final class TestResultFactoryImpl implements TestResultFactory {
    private static final String TEST_NAME = "testName";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";
    private static final String THROWABLE = "throwable";
    private static final String TEST_STATUS = "testStatus";
    private UUID runId;

    /**
     * @param runId - the UUID assigned to this invocation of the DiagnosticTestService.
     */
    public TestResultFactoryImpl(UUID runId) {
        this.runId = runId;
    }

    @Override
    public TestResult pass(String testName, Instant startTime, Instant endTime) {
        logTestResultCommonFields(testName, startTime, endTime);
        MDC.put(TEST_STATUS, TestStatus.PASS.name());
        return new PassTestResultImpl(runId, testName,startTime, endTime);
    }

    @Override
    public TestResult fail(String testName, String exceptionMessage, Instant startTime, Instant endTime) {
        logTestResultCommonFields(testName, startTime, endTime);
        MDC.put(EXCEPTION_MESSAGE, exceptionMessage);
        MDC.put(TEST_STATUS, TestStatus.FAIL.name());
        return new FailureTestResultImpl(runId, testName, exceptionMessage,startTime, endTime);
    }

    @Override
    public TestResult error(String testName, Throwable throwable, Instant startTime, Instant endTime) {
        logTestResultCommonFields(testName, startTime, endTime);
        MDC.put(THROWABLE, throwable.toString());
        MDC.put(TEST_STATUS, TestStatus.ERROR.name());
        return new ErrorTestResultImpl(runId, testName, throwable,startTime, endTime);
    }

    private void logTestResultCommonFields(String testName, Instant startTime, Instant endTime) {
        MDC.clear();
        MDC.put(TEST_NAME, testName);
        MDC.put(START_TIME, startTime.toString());
        MDC.put(END_TIME, endTime.toString());
    }
}
