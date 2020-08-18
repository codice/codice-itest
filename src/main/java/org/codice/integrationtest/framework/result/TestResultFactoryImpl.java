/**
 * Copyright (c) Codice Foundation
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.integrationtest.framework.result;

import java.time.Instant;
import java.util.UUID;

import org.codice.integrationtest.api.TestResult;
import org.codice.integrationtest.api.TestResultFactory;

/**
 * Implements TestResultFactory to create appropriate instances of the TestResult interface to the
 * main application.
 */
final class TestResultFactoryImpl implements TestResultFactory {
    private UUID runId;

    /**
     * @param runId - the UUID assigned to this invocation of the DiagnosticTestService.
     */
    public TestResultFactoryImpl(UUID runId) {
        this.runId = runId;
    }

    @Override
    public TestResult pass(String testName, Instant startTime, Instant endTime) {
        return new PassTestResultImpl(runId, testName,startTime, endTime);
    }

    @Override
    public TestResult fail(String testName, String exceptionMessage, Instant startTime, Instant endTime) {
        return new FailureTestResultImpl(runId, testName, exceptionMessage,startTime, endTime);
    }

    @Override
    public TestResult error(String testName, Throwable throwable, Instant startTime, Instant endTime) {
        return new ErrorTestResultImpl(runId, testName, throwable,startTime, endTime);
    }
}
