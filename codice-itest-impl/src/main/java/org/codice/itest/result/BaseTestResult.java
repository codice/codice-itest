/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest.result;

import org.codice.itest.api.TestResult;
import org.codice.itest.api.TestStatus;
import org.springframework.core.style.ToStringCreator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

abstract class BaseTestResult implements TestResult {

    protected ToStringCreator toStringCreator;

    private UUID runId;

    private String testName;

    private TestStatus testStatus;

    private Instant startTime;

    private long duration;

    private Instant endTime;

    protected BaseTestResult(UUID runId, String testName, TestStatus testStatus, Instant startTime,
            Instant endTime) {
        this.runId = runId;
        this.testName = testName;
        this.testStatus = testStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        if(startTime!=null&&endTime!=null)
            this.duration = ChronoUnit.MILLIS.between(startTime,endTime);
        this.toStringCreator = new ToStringCreator(this);
    }

    @Override
    public TestStatus getTestStatus() {
        return this.testStatus;
    }

    @Override
    public String getTestName() {
        return testName;
    }

    @Override
    public Instant getStartTime() {
        return startTime;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public UUID getRunId() {
        return this.runId;
    }
}
