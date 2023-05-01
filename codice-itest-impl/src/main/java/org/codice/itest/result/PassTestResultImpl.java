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

final class PassTestResultImpl extends BaseTestResult {
    protected PassTestResultImpl(UUID runId, String testName, Instant startTime, Instant endTime) {
        super(runId, testName, TestStatus.PASS, startTime,endTime);
    }

    @Override
    public String toString() {
        return toStringCreator.append(super.getRunId())
                .append(super.getTestStatus())
                .append(super.getTestName())
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

        if (!(other instanceof PassTestResultImpl)) {
            return false;
        }

        PassTestResultImpl testResult = (PassTestResultImpl) other;

        return new EqualsBuilder().append(super.getRunId(), testResult.getRunId())
                .append(super.getTestName(), testResult.getTestName())
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
                .append(this.getStartTime())
                .append(this.getDuration())
                .build();
    }
}
