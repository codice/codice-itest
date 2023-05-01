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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.codice.itest.api.TestStatus;

final class FailureTestResultImpl extends BaseTestResult {
    private final String failMessage;
    private final String stackTrace;

    FailureTestResultImpl(UUID runId, String testName, String failMessage, String stackTrace, Instant startTime, Instant endTime) {
        super(runId, testName, TestStatus.FAIL, startTime,endTime);
        this.failMessage = failMessage;
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return toStringCreator.append(super.getRunId())
                .append(super.getTestStatus())
                .append(super.getTestName())
                .append(this.getFailMessage())
                .append(this.getStackTrace())
                .append(super.getStartTime())
                .append(this.getDuration())
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other == this) {
            return true;
        }

        if (!(other instanceof FailureTestResultImpl)) {
            return false;
        }

        FailureTestResultImpl testResult = (FailureTestResultImpl) other;

        return new EqualsBuilder().append(super.getRunId(), testResult.getRunId())
                .append(super.getTestName(), testResult.getTestName())
                .append(this.getFailMessage(), testResult.getFailMessage())
                .append(this.getStackTrace(), testResult. getStackTrace())
                .append(super.getTestStatus(), testResult.getTestStatus())
                .append(this.getStartTime(), testResult.getStartTime())
                .append(this.getDuration(), testResult.getDuration())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1313, 4137).append(super.getRunId())
                .append(super.getTestName())
                .append(super.getTestStatus())
                .append(this.getFailMessage())
                .append(this.getStackTrace())
                .append(this.getStartTime())
                .append(this.getDuration())
                .build();
    }

    public String getFailMessage() {
        return this.failMessage;
    }

    private String getStackTrace() { return stackTrace; }
}
